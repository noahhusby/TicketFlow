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

package com.noahhusby.ticketflow.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.HISTORY_ICONS
import com.noahhusby.ticketflow.entities.History
import com.noahhusby.ticketflow.entities.User
import com.noahhusby.ticketflow.ui.theme.surfaceColorAtElevation

class UserHistoryCard(private val history: History, private val user: User, private val renderName: Boolean = true, private val modifier: Modifier = Modifier.height(96.dp).padding(vertical = 5.dp).width(350.dp)) {

    @Composable
    fun render() {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(10.dp),
            backgroundColor = surfaceColorAtElevation(2.dp),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (renderName) {
                        userHead(user.name)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(user.name, style = MaterialTheme.typography.labelMedium)
                            Text(user.username, style = MaterialTheme.typography.labelSmall)
                        }
                        Spacer(Modifier.width(24.dp))
                    }
                    Column {
                        Text(history.message, style = MaterialTheme.typography.labelLarge)
                        Text(history.formattedDate, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Icon(HISTORY_ICONS[history.type]!!, modifier = Modifier.padding(6.dp), contentDescription = "Action", tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}