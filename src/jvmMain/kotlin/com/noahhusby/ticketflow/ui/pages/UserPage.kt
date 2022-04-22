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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.TicketFlow
import com.noahhusby.ticketflow.ui.elements.UserCell

class UserPage : Page {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun gui(instance: TicketFlow) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {},
                    text = {
                        Text("New User")
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )
            }
        ) {
            users(instance)
        }
    }

    @Composable
    private fun users(instance: TicketFlow) {
        Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp), tonalElevation = 1.dp) {
            Surface(Modifier.padding(48.dp)) {
                Column {
                    Text("Users", style = MaterialTheme.typography.displayLarge, modifier = Modifier.wrapContentHeight())
                    Row(Modifier.fillMaxSize()) {
                        // Users column
                        Column(Modifier.fillMaxHeight().wrapContentWidth()) {
                            for (user in instance.userHandler.users.values) {
                                UserCell(user).render()
                            }
                        }
                        // Data column
                        Column(Modifier.fillMaxWidth()) { }
                    }
                }
            }
        }
    }
}