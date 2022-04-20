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

package com.noahhusby.ticketflow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.TicketFlow

class MainWindow(val instance: TicketFlow, val startedDarkMode: Boolean, val toggleDarkMode: () -> Boolean) {

    @Composable
    fun gui() {
        var currentPage by remember { mutableStateOf(Pages.HOME) }
        var darkModeIcon by remember { mutableStateOf(startedDarkMode) }
        Row(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface).fillMaxSize()) {
            var selectedItem by remember { mutableStateOf(0) }
            Column(Modifier.width(80.dp).fillMaxHeight().padding(vertical = 36.dp)) {
                NavigationRail(Modifier.weight(0.5f)) {
                    Pages.values().forEachIndexed { index, page ->
                        NavigationRailItem(
                            icon = { Icon(page.icon, contentDescription = null) },
                            label = { Text(page.prettyName) },
                            selected = selectedItem == index,
                            onClick = {
                                currentPage = page
                                selectedItem = index
                            }
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize().weight(0.5f)
                ) {
                    IconButton(onClick = { darkModeIcon = toggleDarkMode.invoke() }) {
                        Icon(
                            imageVector = if (!darkModeIcon) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Dark Mode",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            currentPage.page.gui(instance)
        }
    }
}