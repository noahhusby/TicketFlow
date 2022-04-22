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
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.TicketFlow
import com.noahhusby.ticketflow.UserHandler
import com.noahhusby.ticketflow.entities.User
import com.noahhusby.ticketflow.ui.elements.UserCard
import com.noahhusby.ticketflow.ui.elements.dialog
import com.noahhusby.ticketflow.ui.theme.warningButtonColors
import java.util.*

class UserPage : Page {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun render(instance: TicketFlow) {
        val users = remember { mutableStateListOf<User>() }
        users.removeAll { true }
        users.addAll(instance.userHandler.users.values)
        var isDeleteUserDialogOpen by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(-1) }

        if (isDeleteUserDialogOpen) {
            dialog(
                onCloseRequest = { isDeleteUserDialogOpen = false },
                height = 125.dp,
                width = 500.dp
            ) {
                deleteUserDialog(users[selectedIndex], onCloseDialog = { isDeleteUserDialogOpen = false }, onUserDelete = {
                    isDeleteUserDialogOpen = false
                    selectedIndex = -1
                    users.removeAll { true }
                    users.addAll(instance.userHandler.users.values)
                })
            }
        }


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
                                users.forEachIndexed { index, user ->
                                    UserCard(
                                        user,
                                        index == selectedIndex,
                                        onSelection = {
                                            if (selectedIndex == index) {
                                                selectedIndex = -1
                                            } else {
                                                selectedIndex = index
                                            }
                                        }
                                    ).render()
                                }
                            }

                            // Data column
                            if (selectedIndex != -1) {
                                val user = users[selectedIndex]
                                Surface(Modifier.fillMaxSize().padding(horizontal = 25.dp, vertical = 10.dp), tonalElevation = 2.dp, shadowElevation = 1.dp, shape = RoundedCornerShape(10.dp)) {
                                    Column(Modifier.fillMaxSize().padding(25.dp), verticalArrangement = Arrangement.SpaceBetween) {
                                        Column() {
                                            Row(Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Text(user.name, style = MaterialTheme.typography.headlineSmall)
                                                if (user.isAdmin) {
                                                    Icon(Icons.Outlined.Shield, modifier = Modifier.padding(6.dp), contentDescription = "Administrator", tint = MaterialTheme.colorScheme.onSurface)
                                                }
                                            }
                                            Text("Username: " + user.username + " | Created At: " + user.formattedDate, style = MaterialTheme.typography.labelMedium)
                                            //History
                                            Surface(Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = 40.dp), tonalElevation = 3.dp, shadowElevation = 1.dp, shape = RoundedCornerShape(10.dp)) {
                                                Column(Modifier.padding(20.dp)) {
                                                    Text("User History", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.wrapContentHeight())
                                                }
                                            }
                                        }

                                        Row(Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically) {
                                            FilledTonalButton(
                                                onClick = {
                                                },
                                            ) {
                                                Text(text = "Edit User")
                                            }
                                            if (!user.username.equals("admin")) {
                                                Spacer(Modifier.width(16.dp))
                                                OutlinedButton(
                                                    onClick = {
                                                        isDeleteUserDialogOpen = true
                                                    }
                                                ) {
                                                    Text("Delete User", color = MaterialTheme.colorScheme.error)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun deleteUserDialog(user: User, onCloseDialog: () -> Unit, onUserDelete: () -> Unit) {
        Surface(shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxSize()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Text("Are you sure you want to delete " + user.name + "?")
                Row(Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = {
                            onCloseDialog.invoke()
                        }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.dp))
                    FilledTonalButton(
                        onClick = {
                            UserHandler.getInstance().removeUser(user)
                            onUserDelete.invoke()
                        },
                        colors = warningButtonColors()
                    ) {
                        Text(text = "Yes")
                    }
                }
            }
        }
    }
}