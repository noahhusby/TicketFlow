/*
 * TicketFlow Copyright (C) 2022 Noah Husby
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahhusby.ticketflow;

import com.noahhusby.lib.data.sql.Credentials;
import com.noahhusby.lib.data.sql.actions.Custom;
import com.noahhusby.lib.data.sql.actions.Query;
import com.noahhusby.lib.data.sql.actions.Result;
import com.noahhusby.lib.data.sql.actions.Row;
import com.noahhusby.lib.data.sql.actions.Select;
import com.noahhusby.ticketflow.entities.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Noah Husby
 */
public class Dao {

    private static final Dao instance = new Dao();

    /**
     * Gets the singleton instance of the database handler.
     *
     * @return Instance of {@link Dao}
     */
    public static Dao getInstance() {
        return instance;
    }

    private final Credentials credentials;
    private HikariDataSource ds;

    protected Dao() {
        credentials = new Credentials(TicketFlowConfig.DB_HOST, TicketFlowConfig.DB_PORT, TicketFlowConfig.DB_USERNAME, TicketFlowConfig.DB_PASSWORD, TicketFlowConfig.DB_NAME);
        new Select(null, null, null).query();
    }


    public void connect() {
        HikariConfig config = credentials.toHikariConfig("jdbc:mysql://");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("verifyServerCertificate", false);
        config.addDataSourceProperty("allowPublicKeyRetrieval", true);
        config.addDataSourceProperty("useSSL", false);
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {

            return ds.getConnection();
        } catch (SQLException e) {
            TicketFlow.getLogger().error("Failed to get database connection.", e);
            return null;
        }
    }

    public void close() {
        ds.close();
    }

    public boolean execute(Query query) {
        Connection con = getConnection();
        if (con == null) {
            TicketFlow.getLogger().warn("Connection is null. Cancelling execution.");
            return false;
        }
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(query.query());
        } catch (SQLException e) {
            TicketFlow.getLogger().error("Error while executing statement.");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    con.close();
                    return true;
                } catch (SQLException e) {
                    TicketFlow.getLogger().error("Error while closing statement.");
                }
            }
        }
        try {
            con.close();
        } catch (SQLException e) {
            TicketFlow.getLogger().error("Error while closing connection.");
        }
        return false;
    }

    public Result select(Select select) {
        Connection con = getConnection();
        if (con == null) {
            TicketFlow.getLogger().error("Connection is null. Cancelling execution.");
            return null;
        }
        PreparedStatement stmt;
        ResultSet res;
        try {
            stmt = con.prepareStatement(select.query());
            res = stmt.executeQuery();
            ResultSetMetaData resmeta = res.getMetaData();
            Result result = new Result();
            while (res.next()) {
                Row row = new Row();
                int i = 1;
                boolean bound = true;
                while (bound) {
                    try {
                        boolean addedJson = false;
                        if (!addedJson) {
                            row.addColumn(resmeta.getColumnName(i), res.getObject(i));
                        }

                    } catch (SQLException e) {
                        bound = false;
                    }

                    i++;
                }
                result.addRow(row);
            }

            res.close();
            stmt.close();
            con.close();
            return result;
        } catch (SQLException e) {
            TicketFlow.getLogger().error("Error while running SQL query.", e);
            try {
                con.close();
            } catch (SQLException conCloseEx) {
                TicketFlow.getLogger().error("Error while closing connection.", e);
            }
            return null;
        }
    }

    /**
     * Creates the required tables
     */
    public void createTables() {
        final String createTicketsTable = "CREATE TABLE IF NOT EXISTS n_husb_tickets(id INT AUTO_INCREMENT PRIMARY KEY, issuer VARCHAR(36), description VARCHAR(200))";
        final String createUsersTable = "CREATE TABLE IF NOT EXISTS n_husb_users(uuid VARCHAR(36) PRIMARY KEY, username VARCHAR(30), password VARCHAR(30), name VARCHAR(70), admin int)";
        final String createTicketStatusTable = "CREATE TABLE IF NOT EXISTS n_husb_status(id INT, time LONG, user VARCHAR(36), status VARCHAR(24))";

        TicketFlow.getLogger().debug("Creating tickets table ...");
        execute(new Custom(createTicketsTable));
        TicketFlow.getLogger().debug("Creating users table ...");
        execute(new Custom(createUsersTable));
        TicketFlow.getLogger().debug("Creating status table ...");
        execute(new Custom(createTicketStatusTable));
        TicketFlow.getLogger().debug("All tables have been created.");
    }

    public User authenticateUser(String username, String password) throws IOException {
        Result result = select(new Select("n_husb_users", "*", "username='" + username + "'"));
        if (result == null) {
            throw new IOException("Failed to fetch user");
        }
        if (result.getRows().isEmpty()) {
            return null;
        }
        Row row = result.getRows().get(0);
        String storedPass = (String) row.get("password");
        if (storedPass == null || !storedPass.equals(password)) {
            return null;
        }
        int admin = (int) row.get("admin");
        return new User(UUID.fromString((String) row.get("uuid")), (String) row.get("username"), (String) row.get("name"), admin == 1);
    }

    public void addUsers() {
        // add list of users from userlist.csv file to users table

        // variables for SQL Query inserts
        String sql;

        Statement statement;
        BufferedReader br;
        List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

        // read data from file
        try {
            br = new BufferedReader(new FileReader(new File("./userlist.csv")));

            String line;
            while ((line = br.readLine()) != null) {
                array.add(Arrays.asList(line.split(",")));
            }
        } catch (Exception e) {
            System.out.println("There was a problem loading the file");
        }

        try {

            // Setup the connection with the DB

            statement = getConnection().createStatement();

            // create loop to grab each array index containing a list of values
            // and PASS (insert) that data into your User table
            for (List<String> rowData : array) {

                sql = "insert into jpapa_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
                      + rowData.get(1) + "','" + rowData.get(2) + "');";
                statement.executeUpdate(sql);
            }
            System.out.println("Inserts completed in the given database...");

            // close statement object
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    public int insertRecords(String ticketName, String ticketDesc) {

        int id = 0;
        try {
            statement = getConnection().createStatement();
            statement.executeUpdate("Insert into jpapa_tickets" + "(ticket_issuer, ticket_description) values(" + " '"
                                    + ticketName + "','" + ticketDesc + "')", Statement.RETURN_GENERATED_KEYS);

            // retrieve ticket id number newly auto generated upon record insertion
            ResultSet resultSet = null;
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                // retrieve first field in table
                id = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return id;

    }

    public ResultSet readRecords() {

        ResultSet results = null;
        try {
            //statement = connect.createStatement();
            results = statement.executeQuery("SELECT * FROM jpapa_tickets");
            //connect.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return results;
    }
    // continue coding for updateRecords implementation

    // continue coding for deleteRecords implementation

     */
}