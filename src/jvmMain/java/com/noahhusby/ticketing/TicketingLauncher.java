package com.noahhusby.ticketing;

import com.noahhusby.lib.application.config.Configuration;
import com.noahhusby.lib.application.config.exception.ClassNotConfigException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketingLauncher {

    @Getter
    private final Logger logger = LoggerFactory.getLogger("Ticketing");

    public void start() {
        logger.info("Starting Ticketing ...");

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

    public static void main(String[] args) {
        new Thread(() -> new TicketingLauncher().start()).start();
    }
}