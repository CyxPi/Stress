package com.example.stress.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.stress.domain.model.StressEntry
import com.example.stress.ui.theme.SageGreen

/**
 * Simple line chart displaying stress level history.
 * Custom Canvas implementation for reliable cross-version compatibility.
 */
@Composable
fun StressHistoryChart(
    entries: List<StressEntry>,
    modifier: Modifier = Modifier
) {
    if (entries.isEmpty()) return

    val sortedEntries = entries.sortedBy { it.date }
    val maxStress = sortedEntries.maxOf { it.stressLevel }.coerceAtLeast(1)
    val minStress = sortedEntries.minOf { it.stressLevel }.coerceAtMost(10)

    val lineColor = SageGreen

    Canvas(modifier = modifier.padding(8.dp)) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val chartWidth = width - padding * 2
        val chartHeight = height - padding * 2

        val yRange = (maxStress - minStress).toFloat().coerceAtLeast(1f)
        val stepX = if (sortedEntries.size > 1) chartWidth / (sortedEntries.size - 1) else chartWidth

        val path = Path()
        sortedEntries.forEachIndexed { index, entry ->
            val x = padding + index * stepX
            val y = padding + chartHeight - ((entry.stressLevel - minStress) / yRange) * chartHeight

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw data points
        sortedEntries.forEachIndexed { index, entry ->
            val x = padding + index * stepX
            val y = padding + chartHeight - ((entry.stressLevel - minStress) / yRange) * chartHeight
            drawCircle(
                color = lineColor,
                radius = 6.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}
