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

import com.noahhusby.lib.application.config.Configuration;
import com.noahhusby.lib.application.config.exception.ClassNotConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The primary class representing the backend of the program.
 *
 * @author Noah Husby
 */
public class TicketFlow {

    private static final Logger logger = LoggerFactory.getLogger("Ticketing");

    public static Logger getLogger() {
        return logger;
    }

    /**
     * Starts the backend
     */
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

    /**
     * Gets the user handler.
     *
     * @return {@link UserHandler}
     */
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