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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.HistoryHandler
import com.noahhusby.ticketflow.UserHandler
import com.noahhusby.ticketflow.ui.elements.UserHistoryCard

class HistoryPage : Page {

    @Composable
    override fun render() {
        Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp), tonalElevation = 1.dp) {
            Surface(Modifier.padding(48.dp)) {
                Column {
                    Text("History", style = MaterialTheme.typography.displayLarge)
                    Surface(Modifier.fillMaxWidth().fillMaxHeight().padding(vertical = 40.dp), tonalElevation = 2.dp, shadowElevation = 1.dp, shape = RoundedCornerShape(10.dp)) {
                        Column(Modifier.padding(20.dp)) {
                            LazyColumn {
                                for (history in HistoryHandler.getInstance().history) {
                                    item {
                                        UserHistoryCard(history.value, UserHandler.getInstance().getUser(history.value.user), modifier = Modifier.height(96.dp).padding(vertical = 5.dp).fillMaxWidth()).render()
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