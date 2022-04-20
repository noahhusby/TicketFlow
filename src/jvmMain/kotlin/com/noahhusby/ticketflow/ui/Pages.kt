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

package com.noahhusby.ticketflow.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplaneTicket
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.noahhusby.ticketflow.ui.pages.HomePage
import com.noahhusby.ticketflow.ui.pages.Page
import com.noahhusby.ticketflow.ui.pages.TicketPage

enum class Pages(val prettyName: String, val icon: ImageVector, val page: Page, val requireAdmin: Boolean) {
    HOME("Home", Icons.Filled.Home, HomePage(), false),
    TICKETS("Tickets", Icons.Filled.AirplaneTicket, TicketPage(),false),
    USERS("Users", Icons.Filled.People, HomePage(), true)
}