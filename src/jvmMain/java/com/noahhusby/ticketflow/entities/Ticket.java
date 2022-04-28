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

package com.noahhusby.ticketflow.entities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * A representation of a ticket object.
 *
 * @author Noah Husby
 */
public class Ticket {
    private final int id;
    private final int issuer;
    private final LocalDateTime opened;
    private String description;
    private LocalDateTime closed;

    public Ticket(int id, int issuer, String description, LocalDateTime opened) {
        this(id, issuer, description, opened, null);
    }

    public Ticket(int id, int issuer, String description, LocalDateTime opened, LocalDateTime closed) {
        this.id = id;
        this.issuer = issuer;
        this.description = description;
        this.opened = opened;
        this.closed = closed;
    }

    /**
     * Gets the difference between now and a specified DateTime.
     *
     * @param dateTime The comparative datetime.
     * @return Difference between now and the specified DateTime.
     */
    private static String getDifference(LocalDateTime dateTime) {
        LocalDateTime current = LocalDateTime.now(ZoneOffset.UTC);
        ;
        long seconds = ChronoUnit.SECONDS.between(dateTime, current);
        long minutes = ChronoUnit.MINUTES.between(dateTime, current);
        long hours = ChronoUnit.HOURS.between(dateTime, current);
        long days = ChronoUnit.DAYS.between(dateTime, current);
        if (seconds < 60) {
            return seconds + " " + (seconds == 1 ? "second" : "seconds");
        } else if (minutes < 60) {
            return minutes + " " + (minutes == 1 ? "minute" : "minutes");
        } else if (hours < 24) {
            return hours + " " + (hours == 1 ? "hour" : "hours");
        } else {
            return days + " " + (days == 1 ? "day" : "days");
        }
    }

    /**
     * Gets the numerical id of the ticket.
     * This is assigned by sequently by the database.
     *
     * @return ID of ticket.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the description of the ticket.
     *
     * @return The description of the ticket.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the ticket in the local cache.
     *
     * @param description The new description of the ticket.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the issuer of the ticket.
     *
     * @return The issuer of the ticket.
     */
    public int getIssuer() {
        return issuer;
    }

    /**
     * Gets whether the ticket is closed or not.
     *
     * @return True if the ticket is closed, false otherwise.
     */
    public boolean isClosed() {
        return closed != null;
    }

    /**
     * Sets whether the ticket is closed in the local cache.
     *
     * @param closed True if closed, false otherwise.
     */
    public void setClosed(boolean closed) {
        if (closed) {
            this.closed = LocalDateTime.now(ZoneOffset.UTC);
        } else {
            this.closed = null;
        }
    }

    /**
     * Gets the difference between now and the ticket opening.
     * Formatted as "30 minutes" for example.
     *
     * @return Difference between now and the ticket opening.
     */
    public String getOpenedDifference() {
        return getDifference(opened);
    }

    /**
     * Gets the difference between now and the ticket closure.
     * Formatted as "30 minutes" for example.
     *
     * @return Difference between now and the ticket closure if ticket is closed, null otherwise.
     */
    public String getClosedDifference() {
        if (closed == null) {
            return null;
        }
        return getDifference(closed);
    }

    /**
     * Gets the date and time of the ticket creation.
     *
     * @return Date and time formatted as a String.
     */
    public String getFormattedOpeningTimestamp() {
        return opened.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
