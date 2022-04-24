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

import com.noahhusby.ticketflow.entities.User;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

/**
 * A handler for authenticating, creating, and removing users.
 *
 * @author Noah Husby
 */
public class UserHandler {
    private static final UserHandler instance = new UserHandler();
    private User authenticatedUser;
    private Map<Integer, User> users = new TreeMap<>();

    public static UserHandler getInstance() {
        return instance;
    }

    /**
     * Logs the current user out.
     */
    public void logout() {
        authenticatedUser = null;
    }

    /**
     * Attempts to authenticate a user given its username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A future of {@link AuthenticationResult}.
     */
    public CompletableFuture<AuthenticationResult> authenticate(String username, String password) {
        CompletableFuture<AuthenticationResult> future = new CompletableFuture<>();
        TicketFlow.getLogger().info(String.format("Attempting to authenticate user: \"%s\".", username));

        // Starts a watchdog in-case DB thread locks up
        Thread authenticationWatchdogThread = new Thread(() -> {
            long start = System.currentTimeMillis();
            //noinspection StatementWithEmptyBody
            while (System.currentTimeMillis() < start + 6000) {
            }
            if (!future.isDone()) {
                future.complete(AuthenticationResult.FAILURE);
                TicketFlow.getLogger().error("Authentication request timed out.");
            }
        });
        authenticationWatchdogThread.setName("TF-Auth-Watchdog");
        authenticationWatchdogThread.setDaemon(true);
        authenticationWatchdogThread.start();

        // Cross-checks authentication request with the database
        Thread authenticationThread = new Thread(() -> {
            try {
                User user = Dao.getInstance().authenticateUser(username, password);
                if (user == null) {
                    future.complete(AuthenticationResult.INVALID);
                    TicketFlow.getLogger().info(String.format("Failed to authenticate user: \"%s\". Invalid credentials.", username));
                    return;
                }
                authenticatedUser = user;
                future.complete(AuthenticationResult.SUCCESS);

                boolean admin = authenticatedUser.isAdmin();
                TicketFlow.getLogger().info(String.format("Successfully authenticated user: \"%s\"", username) + (admin ? " w/ admin privileges." : "."));
            } catch (IOException e) {
                future.complete(AuthenticationResult.FAILURE);
            }
        });
        authenticationThread.setName("TF-Auth");
        authenticationThread.setDaemon(true);
        authenticationThread.start();
        return future;
    }

    /**
     * Edits a user's information and saves it to the DB and cache.
     *
     * @param user     The user to be updated.
     * @param name     The new name of the user.
     * @param username The new username of the user.
     * @param password The new password of the user.
     * @param admin    Whether the user should be an admin or not.
     */
    public void editUser(User user, String name, String username, String password, boolean admin) {
        if (!user.getName().equals(name)) {
            Dao.getInstance().updateUser(user.getId(), "name", name);
            users.get(user.getId()).setName(name);
        }
        if (!user.getUsername().equals(username)) {
            Dao.getInstance().updateUser(user.getId(), "username", username);
            users.get(user.getId()).setUsername(username);
        }
        if (password != null) {
            Dao.getInstance().updateUser(user.getId(), "password", password);
        }
        if (user.isAdmin() != admin) {
            Dao.getInstance().updateUser(user.getId(), "admin", String.valueOf(admin ? 1 : 0));
            users.get(user.getId()).setAdmin(admin);
        }
    }

    /**
     * Checks whether a given username is taken.
     *
     * @param username The username to check.
     * @return True if taken, false otherwise.
     */
    public boolean isUsernameTaken(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new user and saves it to the database / cache.
     *
     * @param username Username of the user.
     * @param password Password of the user.
     * @param name     Name of the user.
     * @param admin    Whether the user should be an admin or not.
     * @return {@link User} if account creation was successful, null otherwise.
     */
    public User createNewUser(String username, String password, String name, boolean admin) {
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return null;
            }
        }
        if (name == null) {
            name = username;
        }
        User user = Dao.getInstance().saveNewUser(username, password, name, admin);
        users.put(user.getId(), user);
        return user;
    }

    /**
     * Removes a user based upon a given user object.
     *
     * @param user The user to be deleted.
     */
    public void removeUser(User user) {
        Dao.getInstance().removeUser(user.getId());
        this.users.remove(user.getId());
    }

    /**
     * Gets a map of users.
     *
     * @return A map of users by their IDs. [sorted in natural order]
     */
    public Map<Integer, User> getUsers() {
        return users;
    }

    /**
     * Inserts a list of stored users. This is used as a cache drop on start-up.
     *
     * @param userList A list of users to be inserted into the cache.
     */
    protected void writeToCache(List<User> userList) {
        Map<Integer, User> temp = new TreeMap<>();
        userList.forEach(user -> temp.put(user.getId(), user));
        this.users = temp;
    }

    /**
     * Gets the currently authenticated user.
     *
     * @return {@link User} representation of current user.
     */
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * Gets a user by their id.
     *
     * @param id The id of the user.
     * @return {@link User}.
     */
    public User getUser(int id) {
        return users.get(id);
    }
}
