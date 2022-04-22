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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.TicketFlow
import com.noahhusby.ticketflow.entities.User
import com.noahhusby.ticketflow.ui.elements.UserCard
import java.util.*

class UserPage : Page {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun gui(instance: TicketFlow) {
        var users = remember { mutableStateListOf<User>() }
        users.removeAll { true }
        users.addAll(instance.userHandler.users.values)
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        val temp = Random().nextInt()
                        instance.userHandler.createNewUser("Joe User$temp", "password01", "Joe User$temp", temp % 2 == 0)
                        users.removeAll { true }
                        users.addAll(instance.userHandler.users.values)
                    },
                    text = {
                        Text("New User")
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )
            }
        ) {
            Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp), tonalElevation = 1.dp) {
                Surface(Modifier.padding(48.dp)) {
                    Column {
                        Text("Users", style = MaterialTheme.typography.displayLarge, modifier = Modifier.wrapContentHeight())
                        Text("Select a user to edit.", style = MaterialTheme.typography.labelLarge, modifier = Modifier.wrapContentHeight(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(Modifier.fillMaxSize()) {
                            // Users column
                            Column(Modifier.fillMaxHeight().wrapContentWidth()) {
                                for (user in users) {
                                    UserCard(user).render()
                                }
                            }

                            // Data column
                            Surface(Modifier.fillMaxSize().padding(horizontal = 25.dp, vertical = 10.dp), tonalElevation = 2.dp, shadowElevation = 1.dp, shape = RoundedCornerShape(10.dp)) { }
                            Column(Modifier.fillMaxWidth()) { }
                        }
                    }
                }
            }
        }
    }
}