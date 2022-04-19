package com.noahhusby.ticketflow;

import java.util.concurrent.CompletableFuture;
import lombok.Getter;

/**
 * @author Noah Husby
 */
public class UserHandler {
    @Getter
    private static final UserHandler instance = new UserHandler();

    public CompletableFuture<AuthenticationResult> attemptLogin(String username, String password) {
        CompletableFuture<AuthenticationResult> future = new CompletableFuture<>();
        TicketFlow.getLogger().info("Attempting to authenticate user: " + username);
        Thread temp = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete(AuthenticationResult.INVALID);
        });
        temp.setDaemon(true);
        temp.start();
        return future;
    }
}