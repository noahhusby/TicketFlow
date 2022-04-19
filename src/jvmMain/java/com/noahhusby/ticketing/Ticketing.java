package com.noahhusby.ticketing;

import androidx.compose.ui.text.StringKt;
import com.noahhusby.lib.application.config.Configuration;
import com.noahhusby.lib.application.config.exception.ClassNotConfigException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ticketing {

    @Getter
    private static final Logger logger = LoggerFactory.getLogger("Ticketing");

    public void start() {
        logger.info("Starting Ticketing Backend ...");

        // Load config
        try {
            logger.info("Loading configuration ...");
            Configuration configuration = Configuration.of(TicketingConfig.class);
            configuration.sync(TicketingConfig.class);
            logger.info("Successfully loaded configuration ...");
        } catch (ClassNotConfigException ignored) {
            // I wrote the config library. This exception won't be thrown, so it can be ignored (:
        }
    }

    public UserHandler getUserHandler() {
        return UserHandler.getInstance();
    }

    /**
     * Starts the java backend from the Kotlin frontend.
     */
    public static Ticketing startJavaBackend() {
        Ticketing ticketing = new Ticketing();
        Thread javaBackend = new Thread(ticketing::start);
        javaBackend.setName("Ticketing-Java");
        javaBackend.start();
        return ticketing;
    }
}