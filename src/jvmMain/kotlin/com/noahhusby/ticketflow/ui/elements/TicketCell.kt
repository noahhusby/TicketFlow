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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
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

const val idWeight = .2f
const val descriptionWeight = .5f
const val actionWeight = .3f


class TicketCell(val ticket: Ticket) {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun render() {
        var hover by remember { mutableStateOf(false) }
        Surface(Modifier.fillMaxWidth()
            .height(64.dp)
            .onPointerEvent(PointerEventType.Enter) { hover = true }
            .onPointerEvent(PointerEventType.Exit) { hover = false },
            color = if (hover) surfaceColorAtElevation(5.dp) else Color.Transparent
        ) {
            Column(Modifier.fillMaxSize()) {
                Surface(modifier = Modifier.height(63.dp).fillMaxWidth(), color = Color.Transparent) {
                    Row(Modifier.fillMaxSize().padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row() {
                            Spacer(Modifier.width(2.dp))
                            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                                Icon(Icons.Filled.ConfirmationNumber, contentDescription = "Ticket", tint = if (ticket.isClosed) Color.Red else tf_ticket_open)
                            }
                            Spacer(Modifier.width(6.dp))
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(ticket.description)
                                var text = "#" + ticket.id + " by " + UserHandler.getInstance().getUser(ticket.issuer).name + " was "
                                text += (if (ticket.isClosed) "closed " + ticket.closedDifference else "opened " + ticket.openedDifference) + " ago"
                                Text(text, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Row() {
                            IconButton(onClick = {}) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
                            }
                            Spacer(Modifier.width(8.dp))
                            IconButton(onClick = {}) {
                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
                            }
                            Spacer(Modifier.width(4.dp))
                            FilledTonalButton(onClick = {}) {
                                Text(text = if (ticket.isClosed) "Open Ticket" else "Close Ticket")
                            }
                        }
                    }
                }
                Surface(modifier = Modifier.height(1.dp).fillMaxWidth(), color = MaterialTheme.colorScheme.outline) { }
            }
        }
    }
}