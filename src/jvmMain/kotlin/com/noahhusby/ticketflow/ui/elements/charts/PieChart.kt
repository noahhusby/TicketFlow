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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.noahhusby.ticketflow.ui.elements.charts.renderer.SimpleSliceDrawer

@Composable
fun PieChart(
    pieChartData: PieChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = TweenSpec(durationMillis = 500)
) {
    val transitionProgress = remember(pieChartData.slices) { Animatable(initialValue = 0f) }

    LaunchedEffect(pieChartData.slices) {
        transitionProgress.animateTo(1f, animationSpec = animation)
    }

    DrawChart(
        pieChartData = pieChartData,
        modifier = modifier.fillMaxSize(),
        progress = transitionProgress.value
    )
}

@Composable
private fun DrawChart(
    pieChartData: PieChartData,
    modifier: Modifier,
    progress: Float
) {
    val sliceDrawer = SimpleSliceDrawer()
    val slices = pieChartData.slices

    Canvas(modifier = modifier) {
        drawIntoCanvas {
            var startArc = 0f

            slices.forEach { slice ->
                val arc = 360.0f * (slice.value * progress) / pieChartData.totalSize

                sliceDrawer.drawSlice(
                    canvas = drawContext.canvas,
                    area = drawContext.size,
                    startAngle = startArc,
                    sweepAngle = arc,
                    slice = slice
                )

                startArc += arc
            }
        }
    }
}
