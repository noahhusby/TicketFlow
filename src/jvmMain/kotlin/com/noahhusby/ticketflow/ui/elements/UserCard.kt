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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.entities.User
import com.noahhusby.ticketflow.ui.theme.surfaceColorAtElevation

class UserCard(val user: User) {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun render() {
        var isSelected by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier.height(96.dp).width(350.dp).padding(vertical = 10.dp),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = surfaceColorAtElevation(2.dp),
            contentColor = MaterialTheme.colorScheme.onSurface,
            border = BorderStroke(2.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
            onClick = {
                isSelected = !isSelected
            }
        ) {
            Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    userHead(user.name)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(user.name, style = MaterialTheme.typography.labelMedium)
                        Text(user.username, style = MaterialTheme.typography.labelSmall)
                    }
                }
                if (user.isAdmin) {
                    Icon(Icons.Default.Shield, modifier = Modifier.padding(6.dp), contentDescription = "Administrator", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}