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

package com.noahhusby.ticketflow

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

const val USERNAME_REGEX = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$"
const val PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"

const val ADD_USER_TAKEN_ERROR = "Username is taken. Please choose another username."
const val ADD_USER_USERNAME_SUPPORTS = "Username is invalid. Valid characters include \"[a-zA-Z0-9._]\"."
const val ADD_USER_PASSWORD_INVALID = "Password must have minimum eight characters, at least one letter and one number."
const val ADD_USER_PASSWORDS_DONT_MATCH = "Passwords don't match."

const val DB_HOST = "www.papademas.net"
const val DB_PORT = 3307
const val DB_USERNAME = "fp411"
const val DB_PASSWORD = "411"
const val DB_NAME = "tickets"

const val DB_TICKETS_TABLE = "nhusb_tickets"
const val DB_USERS_TABLE = "nhusb_users"
const val DB_HISTORY_TABLE = "nhusb_history"

// I disagree with this approach to storing tickets, but it is what it is. Ticket status [open / closed] is determined by whether "closed" is null or not.
// An alternative would be to use a history-based system to track all administrators / moderators that have interacted with the ticket. Similar to a GitHub issue.
const val DB_CREATE_TICKETS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS $DB_TICKETS_TABLE(id INT AUTO_INCREMENT PRIMARY KEY, issuer INT, description VARCHAR(200), opened DATETIME DEFAULT CURRENT_TIMESTAMP, closed DATETIME)"
const val DB_CREATE_USERS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS $DB_USERS_TABLE(id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(30), password VARCHAR(30), name VARCHAR(70), admin int, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
const val DB_CREATE_HISTORY_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS $DB_HISTORY_TABLE(created_at DATETIME DEFAULT CURRENT_TIMESTAMP, user INT, type VARCHAR(255), msg VARCHAR(255))"

val HISTORY_ICONS = mapOf(
    HistoryType.LOGIN to Icons.Filled.Login,
    HistoryType.LOGOUT to Icons.Filled.Logout,
    HistoryType.USER_ADDED to Icons.Filled.PersonAdd,
    HistoryType.USER_EDITED to Icons.Filled.ManageAccounts,
    HistoryType.USER_DELETED to Icons.Filled.PersonRemove,
    HistoryType.TICKET_OPENED to Icons.Filled.AddCircle,
    HistoryType.TICKET_EDITED to Icons.Filled.Edit,
    HistoryType.TICKET_DELETED to Icons.Filled.Delete,
    HistoryType.TICKET_CLOSED to Icons.Filled.Delete
)
