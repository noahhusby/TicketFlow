package com.noahhusby.ticketflow;

import com.noahhusby.lib.application.config.Config;

/**
 * A representation of config options.
 * The config is automatically handled by <a href="https://github.com/noahhusby/HusbyLib">HusbyLib</a>.
 *
 * @author Noah Husby
 */
@Config
public class TicketFlowConfig {
    public static String DB_URL = "jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false";
    public static String DB_USERNAME = "fp411";
    public static String DB_PASSWORD = "411";
}
