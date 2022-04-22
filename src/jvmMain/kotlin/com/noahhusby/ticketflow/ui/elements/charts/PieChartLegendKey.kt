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

package com.noahhusby.ticketflow.ui.elements.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

class PieChartLegendKey(val name: String, val color: Color, private var textColor: Color? = null, private var textStyle: TextStyle? = null) {
    @Composable
    fun render() {
        if (textColor == null) {
            textColor = MaterialTheme.colorScheme.onSurface
        }
        if (textStyle == null) {
            textStyle = MaterialTheme.typography.labelLarge
        }
        Row(Modifier.height(20.dp).wrapContentWidth()) {
            Canvas(Modifier.fillMaxSize().weight(0.1f)) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                drawCircle(
                    color = color,
                    center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                    radius = size.minDimension / 4
                )
            }
            Text(name, style = textStyle!!, color = textColor!!, modifier = Modifier.weight(0.9f).fillMaxWidth())
        }

    }
}