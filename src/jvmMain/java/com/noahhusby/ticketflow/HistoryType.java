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

/**
 * An enumeration of possible types for historical entries.
 *
 * @author Noah Husby
 */
public enum HistoryType {
    /*
     * Event fired each time a user signs in.
     */
    LOGIN,

    /*
     * Event fired each time a user signs out.
     */
    LOGOUT,

    /*
     * Event fired each time a user is added.
     */
    USER_ADDED,

    /*
     * Event fired each time a user is edited.
     */
    USER_EDITED,

    /*
     * Event fired each time a user is deleted.
     */
    USER_DELETED,

    /*
     * Event fired each time a ticket is opened.
     */
    TICKET_OPENED,

    /*
     * Event fired each time a ticket is edited.
     */
    TICKET_EDITED,

    /*
     * Event fired each time a ticket is deleted.
     */
    TICKET_DELETED,

    /*
     * Event fired each time a ticket is closed.
     */
    TICKET_CLOSED
}
