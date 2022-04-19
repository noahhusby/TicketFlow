package com.noahhusby.ticketing;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

/**
 * @author Noah Husby
 */
public class UserHandler {
    @Getter
    private static final UserHandler instance = new UserHandler();

    public CompletableFuture<AuthenticationResult> attemptLogin(String username, String password) {
        Ticketing.getLogger().info("Attempting to authenticate user: " + username);
        return CompletableFuture.completedFuture(AuthenticationResult.SUCCESS);
    }
}
