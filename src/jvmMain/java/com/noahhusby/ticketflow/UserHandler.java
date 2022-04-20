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

    public CompletableFuture<AuthenticationResult> attemptLogin(String username, String password) {
        CompletableFuture<AuthenticationResult> future = new CompletableFuture<>();
        TicketFlow.getLogger().info("Attempting to authenticate user: " + username);
        Thread temp = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete(AuthenticationResult.SUCCESS);
        });
        temp.setDaemon(true);
        temp.start();
        return future;
    }
}
