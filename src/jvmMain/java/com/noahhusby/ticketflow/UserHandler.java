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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * A handler for authenticating, creating, and removing users.
 *
 * @author Noah Husby
 */
public class UserHandler {
    private static final UserHandler instance = new UserHandler();

    public static UserHandler getInstance() {
        return instance;
    }

    private User authenticatedUser;

    private Map<Integer, User> users = new LinkedHashMap<>();

    public void logout() {
        authenticatedUser = null;
    }

    public CompletableFuture<AuthenticationResult> authenticate(String username, String password) {
        CompletableFuture<AuthenticationResult> future = new CompletableFuture<>();
        TicketFlow.getLogger().info(String.format("Attempting to authenticate user: \"%s\".", username));
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

    public boolean isUsernameTaken(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public User createNewUser(String username, String password, String name, boolean admin) {
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return null;
            }
        }
        if (name == null) {
            name = username;
        }
        Map.Entry<Integer, LocalDateTime> userEntry = Dao.getInstance().saveNewUser(username, password, name, admin);
        User user = new User(userEntry.getKey(), username, name, admin, userEntry.getValue());
        users.put(user.getId(), user);
        return user;
    }

    public void removeUser(User user) {
        Dao.getInstance().removeUser(user.getId());
        this.users.remove(user.getId());
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    protected void insertStoredUsers(List<User> userList) {
        Map<Integer, User> temp = new HashMap<>();
        userList.forEach(user -> temp.put(user.getId(), user));
        this.users = temp;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
