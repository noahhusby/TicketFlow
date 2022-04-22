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

const val USERNAME_REGEX = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$"
const val PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"

const val ADD_USER_TAKEN_ERROR = "Username is taken. Please choose another username."
const val ADD_USER_USERNAME_SUPPORTS = "Username is invalid. Valid characters include \"[a-zA-Z0-9._]\"."
const val ADD_USER_PASSWORD_INVALID = "Password must have minimum eight characters, at least one letter and one number."
const val ADD_USER_PASSWORDS_DONT_MATCH = "Passwords don't match."