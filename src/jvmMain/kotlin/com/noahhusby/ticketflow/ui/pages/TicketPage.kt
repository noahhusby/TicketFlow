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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.ui.elements.TicketCell

class TicketPage : Page {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun render() {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {},
                    text = {
                        Text("New Ticket")
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )
            }
        ) {
            table()
        }

        //TODO("Not yet implemented")
    }

    @Composable
    private fun table() {
        Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp), tonalElevation = 1.dp) {
            Surface(Modifier.padding(48.dp)) {
                Column {
                    Text("Tickets", style = MaterialTheme.typography.displayLarge)
                }
            }
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    TicketCell().render()
                }
            }
        }
    }
}