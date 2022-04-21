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

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
