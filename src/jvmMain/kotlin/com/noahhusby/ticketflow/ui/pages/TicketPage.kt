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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.TicketFlow
import com.noahhusby.ticketflow.ui.TicketCell

class TicketPage : Page {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun gui(instance: TicketFlow) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {},
                    text = {
                        Text("New Ticket")
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )
            }
        ) {
            table(instance)
        }

        //TODO("Not yet implemented")
    }

    @Composable
    private fun table(instance: TicketFlow) {
        Surface(Modifier.fillMaxSize().padding(40.dp), shape = RoundedCornerShape(10.dp), tonalElevation = 1.dp) {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    TicketCell().gui()
                }
            }
        }
    }

    /*
    @Composable
    fun TableScreen() {
        // Just a fake data... a Pair of Int and String
        val tableData = (1..100).mapIndexed { index, item ->
            index to "Item $index"
        }
        // Each cell of a column must have the same weight.
        val column1Weight = .3f // 30%
        val column2Weight = .7f // 70%
        // The LazyColumn will be our table. Notice the use of the weights below
        LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
            // Here is the header
            item {
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "Column 1", weight = column1Weight)
                    TableCell(text = "Column 2", weight = column2Weight)
                }
            }
            // Here are all the lines of your table.
            items(tableData) {
                val (id, text) = it
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = id.toString(), weight = column1Weight)
                    TableCell(text = text, weight = column2Weight)
                }
            }
        }
    }

     */

}