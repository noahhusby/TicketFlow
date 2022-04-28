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

package com.noahhusby.ticketflow.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.UserHandler
import com.noahhusby.ticketflow.entities.Ticket
import com.noahhusby.ticketflow.ui.theme.surfaceColorAtElevation
import com.noahhusby.ticketflow.ui.theme.tf_ticket_open

class RecentTicketCard(private val ticket: Ticket) {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun render() {
        var hover by remember { mutableStateOf(false) }
        Surface(Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .onPointerEvent(PointerEventType.Enter) { hover = true }
            .onPointerEvent(PointerEventType.Exit) { hover = false },
            color = if (hover) surfaceColorAtElevation(5.dp) else surfaceColorAtElevation(3.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(Modifier.wrapContentHeight().padding(10.dp)) {
                Row {
                    Icon(Icons.Filled.ConfirmationNumber, contentDescription = "Ticket", tint = if (ticket.isClosed) Color.Red else tf_ticket_open)
                    Spacer(Modifier.width(4.dp))
                    Text(ticket.description)
                }
                var text = "#" + ticket.id + " by " + UserHandler.getInstance().getUser(ticket.issuer).name + " was "
                text += (if (ticket.isClosed) "closed " + ticket.closedDifference else "opened " + ticket.openedDifference) + " ago"
                Text(text, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}