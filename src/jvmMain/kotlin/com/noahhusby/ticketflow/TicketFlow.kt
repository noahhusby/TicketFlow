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

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.noahhusby.ticketflow.ui.Login
import com.noahhusby.ticketflow.ui.MainWindow

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

        var isAuthenticated by remember { mutableStateOf(false) }
        if (isAuthenticated) {
            MainWindow(instance).gui()
        } else {
            Login(instance, onAuthentication = {
                isAuthenticated = true
            })
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun test() {


}