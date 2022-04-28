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

package com.noahhusby.ticketflow.ui.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.TicketHandler
import com.noahhusby.ticketflow.UserHandler
import com.noahhusby.ticketflow.entities.Ticket
import com.noahhusby.ticketflow.ui.elements.TicketCell
import com.noahhusby.ticketflow.ui.elements.dialog
import com.noahhusby.ticketflow.ui.theme.surfaceColorAtElevation
import com.noahhusby.ticketflow.ui.theme.ticketingFieldColors

class TicketPage : Page {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun render() {
        val tickets = remember { mutableStateListOf<Ticket>() }
        LaunchedEffect(true) {
            tickets.removeAll { true }
            tickets.addAll(TicketHandler.getInstance().tickets.values)
        }

        var isAddTicketDialogOpen by remember { mutableStateOf(false) }

        if (isAddTicketDialogOpen) {
            dialog(
                onCloseRequest = { isAddTicketDialogOpen = false },
                height = 250.dp,
                width = 500.dp
            ) {
                addTicketDialog(onCloseDialog = { isAddTicketDialogOpen = false }, onTicketAdd = {
                    isAddTicketDialogOpen = false
                    tickets.removeAll { true }
                    tickets.addAll(TicketHandler.getInstance().tickets.values)
                })
            }
        }

        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { isAddTicketDialogOpen = true },
                    text = {
                        Text("New Ticket")
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )
            }
        ) {
            table(tickets)
        }
        // TODO("Not yet implemented")
    }

    @Composable
    private fun table(tickets: SnapshotStateList<Ticket>) {
        Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp), tonalElevation = 1.dp) {
            Surface(Modifier.padding(48.dp)) {
                Column {
                    Text("Tickets", style = MaterialTheme.typography.displayLarge)
                    Surface(Modifier.fillMaxSize().padding(vertical = 30.dp), shape = RoundedCornerShape(30.dp), color = Color.Transparent, border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
                        LazyColumn(Modifier.fillMaxSize()) {
                            item {
                                Surface(Modifier.fillMaxSize().height(48.dp), color = surfaceColorAtElevation(5.dp), border = BorderStroke(0.01.dp, MaterialTheme.colorScheme.outline)) {}
                            }
                            for (ticket: Ticket in tickets) {
                                item {
                                    TicketCell(
                                        ticket,
                                        onTicketEdit = {},
                                        onTicketDelete = {},
                                        onTicketToggleState = {}
                                    ).render()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun addTicketDialog(onCloseDialog: () -> Unit, onTicketAdd: () -> Unit) {
        Surface(shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxSize()) {
            var description by remember { mutableStateOf("") }
            val isFormValid by derivedStateOf { description.isNotEmpty() }

            Column(
                Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.fillMaxWidth().wrapContentHeight()) {
                    Text("Create a new ticket")
                    Spacer(Modifier.height(32.dp))

                    // Description
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(text = "Description") },
                        colors = ticketingFieldColors(),
                        singleLine = true
                    )
                }

                // Action Row
                Row(Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onCloseDialog.invoke() }) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = {
                            TicketHandler.getInstance().createNewTicket(UserHandler.getInstance().authenticatedUser, description)
                            onTicketAdd.invoke()
                        },
                        enabled = isFormValid
                    ) {
                        Text("Create")
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                }
            }
        }
    }
}