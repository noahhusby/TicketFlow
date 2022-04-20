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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.TicketFlow
import com.noahhusby.ticketflow.ui.theme.TicketFlowTheme

class MainWindow(val instance: TicketFlow) {

    @Composable
    fun gui() {
        var currentPage by remember { mutableStateOf(Pages.HOME) }
        TicketFlowTheme {
            Row (modifier = Modifier.background(color = MaterialTheme.colorScheme.surface).fillMaxSize()) {
                var selectedItem by remember { mutableStateOf(0) }
                NavigationRail (
                    modifier = Modifier.wrapContentWidth().padding(vertical = 36.dp),
                ){
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
                currentPage.page.gui(instance)
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
}