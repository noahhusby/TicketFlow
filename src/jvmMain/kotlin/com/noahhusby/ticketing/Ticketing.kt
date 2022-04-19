// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.noahhusby.ticketing

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import com.noahhusby.ticketing.ui.theme.TicketingButtonColors
import com.noahhusby.ticketing.ui.theme.TicketingFieldColors
import com.noahhusby.ticketing.ui.theme.TicketingTheme
import org.jetbrains.skia.paragraph.Alignment


@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    MaterialTheme {
        Surface(color = Color(35, 35, 35), modifier = Modifier.fillMaxSize(), border = BorderStroke(0.1.dp, Color.DarkGray
        ), shape = RoundedCornerShape(5.dp)) {
            Column {
                Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                    Button(onClick = {
                        text = "Hello, Desktop!"
                    }) {
                        Text(text)
                    }
                }
            }
        }
    }
}

@Composable
fun SignInScreen(ticketing: Ticketing) {
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val isFormValid by derivedStateOf {
        username.isNotBlank() && password.length >= 7
    }
    TicketingTheme {
        Surface (Modifier.fillMaxSize(), color = Color(238, 241, 247), shape = RoundedCornerShape(8.dp)) {
            Card(Modifier.fillMaxHeight().requiredWidth(480.dp).padding(vertical = 200.dp), shape = RoundedCornerShape(15.dp)) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    val image: Painter = painterResource("logo.png")
                    Spacer(modifier = Modifier.weight(0.5f))
                    Image(painter = image, contentDescription = "", Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally))
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.weight(0.5f))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = username,
                            onValueChange = { username = it },
                            colors = TicketingFieldColors(),
                            label = { Text(text = "Username") },
                            singleLine = true,
                            trailingIcon = {
                                if (username.isNotBlank())
                                    IconButton(onClick = { username = "" }) {
                                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                                    }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(text = "Password") },
                            colors = TicketingFieldColors(),
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
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val future = ticketing.userHandler.attemptLogin(username, password)
                                future.thenAccept {
                                    when (it) {
                                        AuthenticationResult.FAILURE -> print("x == 1")
                                        AuthenticationResult.INVALID -> print("x == 2")
                                        AuthenticationResult.SUCCESS -> {}
                                    }

                                }
                            },
                            enabled = isFormValid,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TicketingButtonColors()
                        ) {
                            Text(text = "Log In")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("© Husby Labs", color = Color.Gray)
                            Text("v1.0", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WindowScope.AppWindowTitleBar() = WindowDraggableArea {
    Box(Modifier.fillMaxWidth().height(48.dp).background(Color.Transparent))
}


fun main() = application {
    val ticketing = Ticketing.startJavaBackend()
    val state = rememberWindowState(placement = WindowPlacement.Maximized)
    Window(onCloseRequest = ::exitApplication, undecorated = false, resizable = true, transparent = false, state = state) {
        SignInScreen(ticketing)
        AppWindowTitleBar()
    }
}
