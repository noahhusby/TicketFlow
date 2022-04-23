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
import java.time.format.DateTimeFormatter;

/**
 * A representation of a ticket object.
 *
 * @author Noah Husby
 */
public class Ticket {
    private final int id;
    private final LocalDateTime createdAt;
    private final int issuer;
    private String description;
    private boolean closed;

    public Ticket(int id, LocalDateTime createdAt, int issuer, String description, boolean closed) {
        this.id = id;
        this.createdAt = createdAt;
        this.issuer = issuer;
        this.description = description;
        this.closed = closed;
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
        return closed;
    }

    /**
     * Sets whether the ticket is closed in the local cache.
     *
     * @param closed True if closed, false otherwise.
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     * Gets the date and time of the ticket creation.
     *
     * @return Date and time formatted as a String.
     */
    public String getFormattedDate() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
