package com.example.stress.data.local

import com.example.stress.domain.model.StressEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/**
 * Generates sample stress data for testing and demo purposes.
 * Creates realistic-looking entries with varied stress levels.
 */
object SampleDataProvider {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    /**
     * Generates sample entries for the last N days.
     * @param days Number of days to generate (default 14)
     */
    fun generateSampleEntries(days: Int = 14): List<StressEntry> {
        val entries = mutableListOf<StressEntry>()
        var id = 1L

        for (i in 0 until days) {
            val date = LocalDate.now().minusDays(i.toLong())
            val stressLevel = Random.nextInt(2, 9)
            val sleepHours = (4.5f + Random.nextFloat() * 5).coerceIn(4f, 10f)
            val mood = Random.nextInt(1, 6)
            val physicalActivity = Random.nextInt(0, 90)
            val notes = if (Random.nextBoolean() && stressLevel >= 6) {
                listOf(
                    "Busy day at work",
                    "Had a difficult meeting",
                    "Slept poorly",
                    "Good workout helped",
                    "Feeling overwhelmed"
                ).random()
            } else null

            entries.add(
                StressEntry(
                    id = id++,
                    date = date,
                    stressLevel = stressLevel,
                    sleepHours = sleepHours,
                    mood = mood,
                    physicalActivityMinutes = physicalActivity,
                    notes = notes,
                    createdAt = System.currentTimeMillis() - (i * 86400000L)
                )
            )
        }

        return entries.sortedBy { it.date }
    }
}
