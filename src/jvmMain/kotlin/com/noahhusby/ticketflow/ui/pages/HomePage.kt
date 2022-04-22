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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.UserHandler
import com.noahhusby.ticketflow.ui.elements.charts.PieChartData
import com.noahhusby.ticketflow.ui.elements.charts.PieChartLegendKey
import com.noahhusby.ticketflow.ui.elements.charts.pieChart
import com.noahhusby.ticketflow.ui.theme.lightDisplayMedium
import com.noahhusby.ticketflow.ui.theme.onSurfaceColorAtElevation
import com.noahhusby.ticketflow.ui.theme.surfaceColorAtElevation

class HomePage : Page {
    @Composable
    override fun render() {
        Surface(Modifier.padding(48.dp)) {
            Column {
                Row(Modifier.weight(0.2f)) {
                    Column {
                        Text("Dashboard", style = MaterialTheme.typography.displayLarge)
                        Text("Welcome, " + UserHandler.getInstance().authenticatedUser.name + "!", style = lightDisplayMedium)
                    }
                }
                Row(Modifier.weight(0.3f)) {
                    Card(
                        Modifier.fillMaxHeight().weight(0.3f),
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = surfaceColorAtElevation(1.dp),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Row(Modifier.fillMaxSize()) {
                            Column(Modifier.weight(0.3f).padding(top = 25.dp, start = 25.dp)) {
                                Text("Ticket Breakdown", style = MaterialTheme.typography.titleLarge, color = onSurfaceColorAtElevation(1.dp))
                                Column(
                                    modifier = Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    PieChartLegendKey(
                                        name = "Open",
                                        color = MaterialTheme.colorScheme.primary
                                    ).render()
                                    Spacer(Modifier.height(6.dp))
                                    PieChartLegendKey(
                                        name = "Closed",
                                        color = MaterialTheme.colorScheme.tertiary
                                    ).render()
                                }
                            }
                            pieChart(
                                modifier = Modifier.weight(0.6f).padding(40.dp),
                                pieChartData = PieChartData(
                                    slices = listOf(
                                        PieChartData.Slice(29f, MaterialTheme.colorScheme.primary),
                                        PieChartData.Slice(71f, MaterialTheme.colorScheme.tertiary),
                                    )
                                )
                            )
                        }
                    }
                    Spacer(Modifier.weight(0.05f))
                    Card(
                        Modifier.fillMaxHeight().weight(0.3f),
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = surfaceColorAtElevation(1.dp),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {}
                    Spacer(Modifier.weight(0.05f))
                    Card(
                        Modifier.fillMaxHeight().weight(0.3f),
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = surfaceColorAtElevation(1.dp),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {}
                }
                Row(Modifier.weight(0.5f)) {}
            }

        }
    }
}