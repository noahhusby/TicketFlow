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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.*
import com.noahhusby.ticketflow.entities.User
import com.noahhusby.ticketflow.ui.elements.UserCard
import com.noahhusby.ticketflow.ui.elements.dialog
import com.noahhusby.ticketflow.ui.theme.ticketingFieldColors
import com.noahhusby.ticketflow.ui.theme.warningButtonColors
import java.util.*

class UserPage : Page {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun render() {
        val users = remember { mutableStateListOf<User>() }
        LaunchedEffect(true) {
            users.removeAll { true }
            users.addAll(UserHandler.getInstance().users.values)
        }
        var selectedIndex by remember { mutableStateOf(-1) }
        val showUserPanel by derivedStateOf { selectedIndex != -1 }

        var isDeleteUserDialogOpen by remember { mutableStateOf(false) }
        var isAddUserDialogOpen by remember { mutableStateOf(false) }
        var isEditUserDialogOpen by remember { mutableStateOf(false) }

        if (isDeleteUserDialogOpen) {
            dialog(
                onCloseRequest = { isDeleteUserDialogOpen = false },
                height = 125.dp,
                width = 500.dp
            ) {
                if (selectedIndex != -1) {
                    deleteUserDialog(users[selectedIndex], onCloseDialog = { isDeleteUserDialogOpen = false }, onUserDelete = {
                        selectedIndex = -1
                        isDeleteUserDialogOpen = false
                        users.removeAll { true }
                        users.addAll(UserHandler.getInstance().users.values)
                    })
                }
            }
        }

        if (isAddUserDialogOpen) {
            dialog(
                onCloseRequest = { isAddUserDialogOpen = false },
                height = 600.dp,
                width = 500.dp
            ) {
                addUserDialog(onCloseDialog = { isAddUserDialogOpen = false }, onUserAdd = {
                    isAddUserDialogOpen = false
                    users.removeAll { true }
                    users.addAll(UserHandler.getInstance().users.values)
                })
            }
        }

        if (isEditUserDialogOpen) {
            dialog(
                onCloseRequest = { isEditUserDialogOpen = false },
                height = 600.dp,
                width = 500.dp
            ) {
                editUserDialog(users[selectedIndex], onCloseDialog = { isEditUserDialogOpen = false }, onUserAdd = {
                    isEditUserDialogOpen = false
                    users.removeAll { true }
                    users.addAll(UserHandler.getInstance().users.values)
                })
            }
        }

        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { isAddUserDialogOpen = true },
                    text = { Text("New User") },
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
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
                                            selectedIndex = if (selectedIndex == index) {
                                                -1
                                            } else {
                                                index
                                            }
                                        }
                                    ).render()
                                }
                            }

                            // Data column
                            if (showUserPanel) {
                                val user = users[selectedIndex]
                                Surface(Modifier.fillMaxSize().padding(horizontal = 25.dp, vertical = 10.dp), tonalElevation = 2.dp, shadowElevation = 1.dp, shape = RoundedCornerShape(10.dp)) {
                                    Column(Modifier.fillMaxSize().padding(25.dp), verticalArrangement = Arrangement.SpaceBetween) {
                                        Column {
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
                                                    isEditUserDialogOpen = true
                                                },
                                            ) {
                                                Text(text = "Edit User")
                                            }
                                            if (!user.username.equals("admin") && user.id != UserHandler.getInstance().authenticatedUser.id) {
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
    private fun addUserDialog(onCloseDialog: () -> Unit, onUserAdd: () -> Unit) {
        Surface(shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxSize()) {
            var name by remember { mutableStateOf("") }
            var username by remember { mutableStateOf("") }
            var errorText by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var passwordConfirm by remember { mutableStateOf("") }
            var isPasswordVisible by remember { mutableStateOf(false) }
            var isUsernameErrored by remember { mutableStateOf(false) }
            var isPasswordConfirmErrored by remember { mutableStateOf(false) }
            var isAdmin by remember { mutableStateOf(false) }

            val isPasswordValid by derivedStateOf { password.matches(Regex(PASSWORD_REGEX)) }
            val isFormValid by derivedStateOf { username.matches(Regex(USERNAME_REGEX)) && password == passwordConfirm && username.isNotBlank() && isPasswordValid && !UserHandler.getInstance().isUsernameTaken(username) }

            Column(
                Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Login Column
                Column(Modifier.fillMaxWidth().wrapContentHeight()) {
                    Text("Create a new user")
                    Spacer(Modifier.height(48.dp))

                    // Names Row
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier.weight(0.5f),
                            value = name,
                            onValueChange = {
                                if (username.replace(".", " ").equals(name, ignoreCase = true)) {
                                    username = it.replace(" ", ".").lowercase(Locale.US)
                                }
                                name = it
                            },
                            colors = ticketingFieldColors(),
                            label = { Text(text = "Name") },
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.weight(0.5f),
                            value = username,
                            onValueChange = {
                                isUsernameErrored = false
                                username = it
                                val usernameTaken = UserHandler.getInstance().isUsernameTaken(username)
                                isUsernameErrored = usernameTaken
                                if (usernameTaken) {
                                    errorText = ADD_USER_TAKEN_ERROR
                                } else if (errorText == ADD_USER_TAKEN_ERROR) {
                                    errorText = ""
                                }
                            },
                            colors = ticketingFieldColors(),
                            isError = isUsernameErrored || UserHandler.getInstance().isUsernameTaken(username),

                            label = { Text(text = "Username") },
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = {
                            password = it
                            if (!isPasswordValid) {
                                errorText = ADD_USER_PASSWORD_INVALID
                            } else if (errorText == ADD_USER_PASSWORD_INVALID) {
                                errorText = ""
                            }
                        },
                        label = { Text(text = "Password") },
                        colors = ticketingFieldColors(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Password Toggle"
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passwordConfirm,
                        onValueChange = {
                            passwordConfirm = it
                            if (passwordConfirm != password) {
                                errorText = ADD_USER_PASSWORDS_DONT_MATCH
                                isPasswordConfirmErrored = true
                            } else if (errorText == ADD_USER_PASSWORDS_DONT_MATCH) {
                                isPasswordConfirmErrored = false
                                errorText = ""
                            }
                        },
                        isError = isPasswordConfirmErrored,
                        label = { Text(text = "Confirm Password") },
                        colors = ticketingFieldColors(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Password Toggle"
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorText, color = Color.Red, style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentSize()) {
                        Checkbox(checked = isAdmin, onCheckedChange = { b -> isAdmin = b })
                        Text("Administrator")
                    }
                }

                // Action Row
                Row(Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onCloseDialog.invoke() }) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = {
                            UserHandler.getInstance().createNewUser(username, password, name, isAdmin)
                            onUserAdd.invoke()
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

    @Composable
    private fun editUserDialog(user: User, onCloseDialog: () -> Unit, onUserAdd: () -> Unit) {
        Surface(shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxSize()) {
            val isEditingAdministrator = user.username == "admin"

            var name by remember { mutableStateOf(user.name) }

            var username by remember { mutableStateOf(user.username) }
            var errorText by remember { mutableStateOf("") }

            var password by remember { mutableStateOf("") }
            var passwordConfirm by remember { mutableStateOf("") }
            var isPasswordVisible by remember { mutableStateOf(false) }
            var isPasswordConfirmErrored by remember { mutableStateOf(false) }
            var shouldResetPassword by remember { mutableStateOf(false) }

            var isUsernameErrored by remember { mutableStateOf(false) }
            var isAdmin by remember { mutableStateOf(user.isAdmin) }

            val isPasswordValid by derivedStateOf { password.matches(Regex(PASSWORD_REGEX)) }
            val isFormValid by derivedStateOf { username.matches(Regex(USERNAME_REGEX)) && password == passwordConfirm && username.isNotBlank() && (!shouldResetPassword || (isPasswordValid && shouldResetPassword)) && (!UserHandler.getInstance().isUsernameTaken(username) || username == user.username) }

            Column(
                Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Login Column
                Column(Modifier.fillMaxWidth().wrapContentHeight()) {
                    Text("Edit User")
                    Spacer(Modifier.height(48.dp))

                    // Names Row
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier.weight(0.5f),
                            value = name,
                            onValueChange = {
                                if (username.replace(".", " ").equals(name, ignoreCase = true) && user.username != "admin") {
                                    username = it.replace(" ", ".").lowercase(Locale.US)
                                }
                                name = it
                            },
                            colors = ticketingFieldColors(),
                            label = { Text(text = "Name") },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            modifier = Modifier.weight(0.5f),
                            enabled = !isEditingAdministrator,
                            value = username,
                            onValueChange = {
                                isUsernameErrored = false
                                username = it
                                val usernameTaken = UserHandler.getInstance().isUsernameTaken(username) && username != user.username
                                isUsernameErrored = usernameTaken
                                if (usernameTaken) {
                                    errorText = ADD_USER_TAKEN_ERROR
                                } else if (errorText == ADD_USER_TAKEN_ERROR) {
                                    errorText = ""
                                }
                            },
                            colors = ticketingFieldColors(),
                            isError = isUsernameErrored || (UserHandler.getInstance().isUsernameTaken(username) && username != user.username),

                            label = { Text(text = "Username") },
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentSize()) {
                        Checkbox(checked = shouldResetPassword, onCheckedChange = { b ->
                            shouldResetPassword = b
                            if (!b) {
                                password = ""
                                passwordConfirm = ""
                                isPasswordConfirmErrored = false
                                errorText = ""
                            }
                        })
                        Text("Reset Password?")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = shouldResetPassword,
                        value = password,
                        onValueChange = {
                            password = it
                            if (!isPasswordValid) {
                                errorText = ADD_USER_PASSWORD_INVALID
                            } else if (errorText == ADD_USER_PASSWORD_INVALID) {
                                errorText = ""
                            }
                        },
                        label = { Text(text = "Password") },
                        colors = ticketingFieldColors(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(enabled = shouldResetPassword, onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Password Toggle"
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = shouldResetPassword,
                        value = passwordConfirm,
                        onValueChange = {
                            passwordConfirm = it
                            if (passwordConfirm != password) {
                                errorText = ADD_USER_PASSWORDS_DONT_MATCH
                                isPasswordConfirmErrored = true
                            } else if (errorText == ADD_USER_PASSWORDS_DONT_MATCH) {
                                isPasswordConfirmErrored = false
                                errorText = ""
                            }
                        },
                        isError = isPasswordConfirmErrored,
                        label = { Text(text = "Confirm Password") },
                        colors = ticketingFieldColors(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(enabled = shouldResetPassword, onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Password Toggle"
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorText, color = Color.Red, style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentSize()) {
                        Checkbox(checked = isAdmin, enabled = !isEditingAdministrator && UserHandler.getInstance().authenticatedUser.id != user.id, onCheckedChange = { b -> isAdmin = b })
                        Text("Administrator")
                    }
                }

                // Action Row
                Row(Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onCloseDialog.invoke() }) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = {
                            UserHandler.getInstance().editUser(user, name, username, if (shouldResetPassword) password else null, isAdmin)
                            onUserAdd.invoke()
                        },
                        enabled = isFormValid
                    ) {
                        Text("Save")
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                }
            }
        }
    }
}