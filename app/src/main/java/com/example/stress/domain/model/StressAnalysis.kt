package com.example.stress.domain.model

import java.time.LocalDate

/**
 * Result of stress trend analysis.
 * Used for dashboard display and pattern detection.
 */
data class StressAnalysis(
    val averageStress: Float,
    val weeklyAverage: Float?,
    val stressPeaks: List<StressPeak>,
    val trend: StressTrend,
    val highStressDays: Int,
    val totalEntries: Int,
    val dateRange: Pair<LocalDate, LocalDate>
)

/**
 * Represents a detected stress peak for highlighting in UI.
 */
data class StressPeak(
    val date: LocalDate,
    val stressLevel: Int,
    val isHighStress: Boolean
)

/**
 * Simple trend direction for UI display.
 * TODO: Replace with ML-based prediction when ML module is added.
 */
enum class StressTrend {
    IMPROVING,
    STABLE,
    WORSENING,
    INSUFFICIENT_DATA
}
