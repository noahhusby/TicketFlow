package com.noahhusby.ticketflow;

import com.noahhusby.lib.application.config.Configuration;
import com.noahhusby.lib.application.config.exception.ClassNotConfigException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketFlow {

    @Getter
    private static final Logger logger = LoggerFactory.getLogger("Ticketing");

    public void start() {
        logger.info("Starting TicketFlow ...");

        // Load config
        try {
            logger.info("Loading configuration ...");
            Configuration configuration = Configuration.of(TicketFlowConfig.class);
            configuration.sync(TicketFlowConfig.class);
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
    public static TicketFlow startJavaBackend() {
        TicketFlow ticketing = new TicketFlow();
        Thread javaBackend = new Thread(ticketing::start);
        javaBackend.setName("TicketFlow");
        javaBackend.start();
        return ticketing;
    }
}