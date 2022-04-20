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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

const val idWeight = .2f
const val descriptionWeight = .5f
const val actionWeight = .3f


class TicketCell {
    @Composable
    fun gui() {
        Row(Modifier.fillMaxWidth().height(16.dp)) {
            Text("1", Modifier.weight(idWeight))
            Text("A thing that does stuff", Modifier.weight(descriptionWeight))
            Text("Button", Modifier.weight(actionWeight))
        }
    }

    @Composable
    fun test() {

    }
}