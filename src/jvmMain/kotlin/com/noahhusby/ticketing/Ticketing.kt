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
import com.noahhusby.ticketing.ui.Login
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
private fun WindowScope.AppWindowTitleBar() = WindowDraggableArea {
    Box(Modifier.fillMaxWidth().height(48.dp).background(Color.Transparent))
}


fun main() = application {
    val ticketing = Ticketing.startJavaBackend()
    val state = rememberWindowState(placement = WindowPlacement.Maximized)
    Window(onCloseRequest = ::exitApplication, undecorated = false, resizable = true, transparent = false, state = state) {
        Login(ticketing)
        AppWindowTitleBar()
    }
}
