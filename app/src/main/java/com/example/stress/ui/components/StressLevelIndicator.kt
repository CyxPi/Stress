package com.example.stress.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.stress.ui.theme.StressHigh
import com.example.stress.ui.theme.StressLow
import com.example.stress.ui.theme.StressMedium

/**
 * Circular indicator showing stress level with color coding.
 */
@Composable
fun StressLevelIndicator(
    level: Int,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp
) {
    val color = when {
        level >= 7 -> StressHigh
        level >= 4 -> StressMedium
        else -> StressLow
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = level.toString(),
            style = MaterialTheme.typography.headlineLarge,
            color = color
        )
    }
}
