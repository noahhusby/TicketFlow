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
 * A representation of a user object.
 *
 * @author Noah Husby
 */
public class User {
    private final int id;
    private final LocalDateTime createdAt;
    private String username;
    private String name;
    private boolean admin;

    public User(int id, String username, String name, boolean admin, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name == null ? username : name;
        this.admin = admin;
        this.createdAt = createdAt;
    }

    /**
     * Gets the numerical id of the user.
     * This is assigned by sequently by the database.
     *
     * @return ID of user.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user in the local cache.
     *
     * @param username The new username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the full name of the user.
     *
     * @return The full name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user in the local cache.
     *
     * @param name The new name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets whether the user has administrative privileges or not.
     * - Users can only open and view their own tickets.
     * - Admins can manage all tickets and users.
     *
     * @return True if the user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets whether the user has administrative privileges in the local cache.
     *
     * @param admin True if admin, false otherwise.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Gets the date and time of the user creation.
     *
     * @return Date and time formatted as a String.
     */
    public String getFormattedDate() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
