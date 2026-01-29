package com.example.stress.domain.usecase

import com.example.stress.domain.model.StressAnalysis
import com.example.stress.domain.model.StressEntry
import com.example.stress.domain.model.StressPeak
import com.example.stress.domain.model.StressTrend
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Rule-based stress analysis use case.
 * Calculates averages, detects peaks, and determines trends.
 *
 * TODO: Add ML-based analysis - this class is designed to be extended
 * or replaced with ML predictions. The interface remains the same.
 */
class StressAnalysisUseCase {

    companion object {
        private const val HIGH_STRESS_THRESHOLD = 7
        private const val MIN_ENTRIES_FOR_TREND = 3
    }

    /**
     * Analyzes stress entries and produces summary statistics.
     * @param entries List of stress entries (typically last 7-30 days)
     * @param dateRange Optional date range for context
     */
    fun analyze(
        entries: List<StressEntry>,
        dateRange: Pair<LocalDate, LocalDate>? = null
    ): StressAnalysis {
        if (entries.isEmpty()) {
            val range = dateRange ?: (LocalDate.now() to LocalDate.now())
            return StressAnalysis(
                averageStress = 0f,
                weeklyAverage = null,
                stressPeaks = emptyList(),
                trend = StressTrend.INSUFFICIENT_DATA,
                highStressDays = 0,
                totalEntries = 0,
                dateRange = range
            )
        }

        val avgStress = entries.map { it.stressLevel }.average().toFloat()
        val highStressDays = entries.groupBy { it.date }.count { (_, dayEntries) ->
            dayEntries.maxOf { it.stressLevel } >= HIGH_STRESS_THRESHOLD
        }

        val peaks = detectStressPeaks(entries)
        val weeklyAvg = calculateWeeklyAverage(entries)
        val trend = determineTrend(entries)

        val range = dateRange ?: run {
            val dates = entries.map { it.date }
            (dates.minOrNull() ?: LocalDate.now()) to (dates.maxOrNull() ?: LocalDate.now())
        }

        return StressAnalysis(
            averageStress = avgStress,
            weeklyAverage = weeklyAvg,
            stressPeaks = peaks,
            trend = trend,
            highStressDays = highStressDays,
            totalEntries = entries.size,
            dateRange = range
        )
    }

    private fun detectStressPeaks(entries: List<StressEntry>): List<StressPeak> {
        val byDate = entries.groupBy { it.date }
        return byDate.map { (date, dayEntries) ->
            val maxStress = dayEntries.maxOf { it.stressLevel }
            StressPeak(
                date = date,
                stressLevel = maxStress,
                isHighStress = maxStress >= HIGH_STRESS_THRESHOLD
            )
        }.sortedByDescending { it.stressLevel }.take(5)
    }

    private fun calculateWeeklyAverage(entries: List<StressEntry>): Float? {
        val weekAgo = LocalDate.now().minusDays(7)
        val recentEntries = entries.filter { it.date >= weekAgo }
        return if (recentEntries.isNotEmpty()) {
            recentEntries.map { it.stressLevel }.average().toFloat()
        } else null
    }

    private fun determineTrend(entries: List<StressEntry>): StressTrend {
        if (entries.size < MIN_ENTRIES_FOR_TREND) return StressTrend.INSUFFICIENT_DATA

        val sorted = entries.sortedBy { it.date }
        val firstHalf = sorted.take(sorted.size / 2)
        val secondHalf = sorted.drop(sorted.size / 2)

        val firstAvg = firstHalf.map { it.stressLevel }.average()
        val secondAvg = secondHalf.map { it.stressLevel }.average()
        val diff = secondAvg - firstAvg

        return when {
            diff < -0.5 -> StressTrend.IMPROVING
            diff > 0.5 -> StressTrend.WORSENING
            else -> StressTrend.STABLE
        }
    }
}
