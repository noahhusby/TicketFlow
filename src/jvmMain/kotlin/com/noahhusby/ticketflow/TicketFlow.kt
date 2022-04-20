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
package com.noahhusby.ticketflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.noahhusby.ticketflow.ui.Login
import com.noahhusby.ticketflow.ui.theme.TicketFlowTheme

fun main() = application {
    val instance = TicketFlow.startJavaBackend()
    val state = rememberWindowState(placement = WindowPlacement.Maximized)
    Window(
        onCloseRequest = ::exitApplication,
        undecorated = false,
        resizable = true,
        transparent = false,
        state = state,
        icon = painterResource("icon.png"),
        title = "TicketFlow"
    ) {
        test()
        /*
        var isAuthenticated by remember { mutableStateOf(false) }
        if (isAuthenticated) {
            // TODO: Main window
            test()
        } else {
            Login(instance, onAuthentication = {
                isAuthenticated = true
            })
        }

         */
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun test() {

    TicketFlowTheme {
        Box (modifier = Modifier.background(color = MaterialTheme.colorScheme.surface).fillMaxSize()) {
            var selectedItem by remember { mutableStateOf(0) }
            val items = listOf("Home", "Search", "Settings")
            val icons = listOf(Icons.Filled.Home, Icons.Filled.Search, Icons.Filled.Settings)
            NavigationRail (
                modifier = Modifier.align(Alignment.CenterStart).padding(vertical = 20.dp),
                header = {
                    FloatingActionButton(onClick = {}) {
                        Icon(Icons.Filled.Search, contentDescription = null)
                    }
                }
            ){
                items.forEachIndexed { index, item ->
                    NavigationRailItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }

        /*
        Surface (
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxSize()
                ){

        }

         */
    }
}