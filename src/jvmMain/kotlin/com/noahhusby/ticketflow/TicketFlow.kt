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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.noahhusby.ticketflow.ui.MainWindow
import com.noahhusby.ticketflow.ui.login
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
        val isSystemInDarkTheme = isSystemInDarkTheme()
        var isDarkMode by remember { mutableStateOf(isSystemInDarkTheme) }
        TicketFlowTheme(isDarkMode) {
            var isAuthenticated by remember { mutableStateOf(false) }
            if (isAuthenticated) {
                MainWindow(instance, isDarkMode, toggleDarkMode = {
                    isDarkMode = !isDarkMode
                    return@MainWindow isDarkMode
                }).gui()
            } else {
                login(instance, onAuthentication = {
                    isAuthenticated = true
                })
            }
        }
    }
}