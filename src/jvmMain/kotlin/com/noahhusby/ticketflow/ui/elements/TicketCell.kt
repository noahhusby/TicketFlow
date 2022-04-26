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
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.ui.theme.surfaceColorAtElevation

const val idWeight = .2f
const val descriptionWeight = .5f
const val actionWeight = .3f


class TicketCell {
    @Composable
    fun render() {
        Card(
            modifier = Modifier.height(96.dp).width(350.dp).padding(vertical = 10.dp),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = surfaceColorAtElevation(2.dp),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    Text("Test")
                }
                Surface(Modifier.width(100.dp).fillMaxHeight(), color = Color.Transparent, border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface), shape = RoundedCornerShape(30.dp)) {
                    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text("Open")
                    }
                }
            }
        }
    }
}