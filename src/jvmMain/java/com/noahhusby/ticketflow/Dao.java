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
import com.noahhusby.lib.data.sql.actions.Insert;
import com.noahhusby.lib.data.sql.actions.Query;
import com.noahhusby.lib.data.sql.actions.Result;
import com.noahhusby.lib.data.sql.actions.Row;
import com.noahhusby.lib.data.sql.actions.Select;
import com.noahhusby.lib.data.sql.actions.Update;
import com.noahhusby.lib.data.sql.actions.UpdateValue;
import com.noahhusby.ticketflow.entities.History;
import com.noahhusby.ticketflow.entities.Ticket;
import com.noahhusby.ticketflow.entities.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility for handling database connections
 *
 * @author Noah Husby
 */
public class Dao {

    private static final Dao instance = new Dao();
    private final Credentials credentials;
    private HikariDataSource ds;

    protected Dao() {
        // Using a few utility classes from HusbyLib, my own library, to keep data structured.
        credentials = new Credentials(TicketFlowConfig.DB_HOST, TicketFlowConfig.DB_PORT, TicketFlowConfig.DB_USERNAME, TicketFlowConfig.DB_PASSWORD, TicketFlowConfig.DB_NAME);
        new Select(null, null, null).query();
    }

    /**
     * Gets the singleton instance of the database handler.
     *
     * @return Instance of {@link Dao}
     */
    public static Dao getInstance() {
        return instance;
    }

    public void connect() {
        // Hikari is a DB pool that helps manage SQL connections in a better way. Syntax for queries remains the same.
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

    /**
     * Gets the SQL connection from the Hikari pool.
     *
     * @return {@link Connection}
     */
    public Connection getConnection() {
        try {

            return ds.getConnection();
        } catch (SQLException e) {
            TicketFlow.getLogger().error("Failed to get database connection.", e);
            return null;
        }
    }

    /**
     * Executes a query.
     *
     * @param query The query to be executed.
     * @return True if correctly executed, false otherwise.
     */
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
            TicketFlow.getLogger().error("Error while executing statement.", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    con.close();
                    //noinspection ReturnInsideFinallyBlock
                    return true;
                } catch (SQLException e) {
                    TicketFlow.getLogger().error("Error while closing statement.", e);
                }
            }
        }
        try {
            con.close();
        } catch (SQLException e) {
            TicketFlow.getLogger().error("Error while closing connection.", e);
        }
        return false;
    }

    /*
     * The method above and below this comment block are nearly indentical in purpose.
     * It's a hacky way to get generated keys.
     *
     * I would really like to use my SQL library for this,
     * but part of the assignment calls for showing the SQL implementation so ...
     */

    /**
     * Executes a query and returns the generated fields.
     *
     * @param query The query to be executed.
     * @return The generated keys from the execution.
     */
    public ResultSet executeAndGetKeys(Query query) {
        Connection con = getConnection();
        if (con == null) {
            TicketFlow.getLogger().warn("Connection is null. Cancelling execution.");
            return null;
        }
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query.query(), Statement.RETURN_GENERATED_KEYS);
            return stmt.getGeneratedKeys();
        } catch (SQLException e) {
            TicketFlow.getLogger().error("Error while executing statement.", e);
        }
        try {
            con.close();
        } catch (SQLException e) {
            TicketFlow.getLogger().error("Error while closing connection.", e);
        }
        return null;
    }

    /**
     * Gets a {@link Result} from a selection query.
     *
     * @param select {@link Select}
     * @return A {@link Result} if query was successful, null otherwise.
     */
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
                        row.addColumn(resmeta.getColumnName(i), res.getObject(i));
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
     * Loads entities from database into the local cache.
     * This allows the interface to be updated without querying the database on recomposition.
     */
    public void loadEntitiesIntoCache() {
        TicketFlow.getLogger().info("Loading entities into cache ...");
        long start = System.currentTimeMillis();

        // Users
        {
            Result users = select(new Select(ConstantsKt.DB_USERS_TABLE, "*", null));
            if (users == null) {
                TicketFlow.getLogger().warn("Failed to load user cache!");
            } else if (!users.getRows().isEmpty()) {
                List<User> tempUsers = new ArrayList<>();
                for (Row row : users.getRows()) {
                    int admin = (int) row.get("admin");
                    tempUsers.add(new User((Integer) row.get("id"), (String) row.get("username"), (String) row.get("name"), admin == 1, (LocalDateTime) row.get("created_at")));
                }
                UserHandler.getInstance().writeToCache(tempUsers);
            }
        }

        // Tickets
        {
            Result tickets = select(new Select(ConstantsKt.DB_TICKETS_TABLE, "*", null));
            if (tickets == null) {
                TicketFlow.getLogger().warn("Failed to load tickets cache!");
            } else if (!tickets.getRows().isEmpty()) {
                List<Ticket> tempTickets = new ArrayList<>();
                for (Row row : tickets.getRows()) {
                    int id = (int) row.get("id");
                    int issuer = (int) row.get("issuer");
                    String description = (String) row.get("description");
                    LocalDateTime opened = (LocalDateTime) row.get("opened");
                    LocalDateTime closed = null;
                    if (row.getKeys().contains("closed")) {
                        closed = (LocalDateTime) row.get("closed");
                    }
                    tempTickets.add(new Ticket(id, issuer, description, opened, closed));
                }
                TicketHandler.getInstance().writeToCache(tempTickets);
            }
        }

        // History
        {
            Result history = select(new Select(ConstantsKt.DB_HISTORY_TABLE, "*", null));
            if (history == null) {
                TicketFlow.getLogger().warn("Failed to load history cache!");
            } else if (!history.getRows().isEmpty()) {
                List<History> tempHistory = new ArrayList<>();
                for (Row row : history.getRows()) {
                    LocalDateTime timestamp = (LocalDateTime) row.get("created_at");
                    int user = (int) row.get("user");
                    HistoryType type = HistoryType.valueOf((String) row.get("type"));
                    String message = (String) row.get("msg");
                    tempHistory.add(new History(timestamp, user, type, message));
                }
                HistoryHandler.getInstance().writeToCache(tempHistory);
            }
        }

        TicketFlow.getLogger().info("Finished loading entities into cache. (" + (System.currentTimeMillis() - start) + "ms)");
    }

    /**
     * Creates the required tables.
     */
    public void createTables() {
        // The queries are located in Constants.kt file (ConstantsKt in java syntax)
        TicketFlow.getLogger().debug("Creating tickets table ...");
        if (!execute(new Custom(ConstantsKt.DB_CREATE_TICKETS_TABLE_QUERY))) {
            TicketFlow.getLogger().warn("Failed to create tickets table!");
        }
        TicketFlow.getLogger().debug("Creating users table ...");
        if (!execute(new Custom(ConstantsKt.DB_CREATE_USERS_TABLE_QUERY))) {
            TicketFlow.getLogger().warn("Failed to create users table!");
        }
        TicketFlow.getLogger().debug("Creating history table ...");
        if (!execute(new Custom(ConstantsKt.DB_CREATE_HISTORY_TABLE_QUERY))) {
            TicketFlow.getLogger().warn("Failed to create history table!");
        }
        TicketFlow.getLogger().debug("All tables have been created.");
    }

    /**
     * Attempts to authenticate a user based upon a given username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return {@link User} if credentials are correct, null otherwise.
     * @throws IOException if there is an error while contacting the database.
     */
    public User authenticateUser(String username, String password) throws IOException {
        Result result = select(new Select(ConstantsKt.DB_USERS_TABLE, "*", "username='" + username + "'"));
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
        return new User((Integer) row.get("id"), (String) row.get("username"), (String) row.get("name"), admin == 1, (LocalDateTime) row.get("created_at"));
    }

    /**
     * Updates a specific column for a specified user.
     *
     * @param id     The id of the user to update.
     * @param column The column name to update.
     * @param value  The new value.
     */
    public void updateUser(int id, String column, String value) {
        if (!execute(new Update(ConstantsKt.DB_USERS_TABLE, new UpdateValue(column, value), "id='" + id + "'"))) {
            TicketFlow.getLogger().warn(String.format("Failed to update user \"%s\" in column \"%s\" with value \"%s\"", id, column, value));
        }
    }

    /**
     * Saves a new user to the database.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param name     The name of the user.
     * @param admin    True if the user has administrative rights, false otherwise.
     * @return An entry consisting of the user's id, and the time of account creation.
     */
    protected User saveNewUser(String username, String password, String name, boolean admin) {
        TicketFlow.getLogger().debug("Saving user: " + name);
        ResultSet set = executeAndGetKeys(new Insert(ConstantsKt.DB_USERS_TABLE, "username,password,name,admin", username, password, name, String.valueOf(admin ? 1 : 0)));
        if (set == null) {
            TicketFlow.getLogger().warn("Failed to save new user: " + name);
        } else {
            try {
                set.next();
                int id = set.getInt(1);
                set.getStatement().close();
                set.close();
                return new User(id, username, name, admin, LocalDateTime.now());
            } catch (SQLException e) {
                TicketFlow.getLogger().error("Error while trying to create new user: ", e);
            }
        }
        return null;
    }

    /**
     * Saves a new ticket for specified issuer with a given description.
     *
     * @param user        The user who issued the ticket.
     * @param description The description of the ticket.
     * @return A new {@link Ticket} object.
     */
    protected Ticket saveNewTicket(User user, String description) {
        TicketFlow.getLogger().debug("Saving new ticket for: " + user.getName());
        ResultSet set = executeAndGetKeys(new Insert(ConstantsKt.DB_TICKETS_TABLE, "issuer,description", String.valueOf(user.getId()), description));
        if (set == null) {
            TicketFlow.getLogger().warn("Failed to save new ticket for: " + user.getName());
        } else {
            try {
                int id = set.getInt("id");
                LocalDateTime opened = set.getObject("opened", LocalDateTime.class);
                return new Ticket(id, user.getId(), description, opened);
                // TODO: History
            } catch (SQLException e) {
                TicketFlow.getLogger().error("Error while trying to save ticket: ", e);
            }
        }
        return null;
    }

    /**
     * Saves a new history event for specified user with a given type.
     *
     * @param user    The user of the action.
     * @param type    {@link HistoryType}.
     * @param message The message of the event.
     * @return New {@link History} instance.
     */
    protected History saveNewHistory(User user, HistoryType type, String message) {
        TicketFlow.getLogger().debug("Saving new history event for: " + user.getName());
        execute(new Insert(ConstantsKt.DB_HISTORY_TABLE, "user,type,msg", String.valueOf(user.getId()), type.name(), message));
        return new History(LocalDateTime.now(), user.getId(), type, message);
    }

    /**
     * Removes a user from the database given its id.
     *
     * @param id ID of user to remove.
     */
    public void removeUser(int id) {
        TicketFlow.getLogger().debug("Removing user by ID: " + id);
        if (!execute(new Custom("DELETE FROM " + ConstantsKt.DB_USERS_TABLE + " WHERE id='" + id + "'"))) {
            TicketFlow.getLogger().warn(String.format("Failed to remove user \"%s\" by ID.", id));
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
     */
}
