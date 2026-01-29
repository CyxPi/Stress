package com.example.stress.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a single stress tracking entry.
 * Stores stress level (1-10) and related factors for trend analysis.
 *
 * @param id Auto-generated primary key
 * @param date Date of the entry (stored as YYYY-MM-DD for easy querying)
 * @param stressLevel Stress level from 1 (low) to 10 (high)
 * @param sleepHours Duration of sleep in hours
 * @param mood Mood rating (1-5 scale: 1=very low, 5=very good)
 * @param physicalActivityMinutes Minutes of physical activity
 * @param notes Optional free-text notes
 */
@Entity(tableName = "stress_entries")
data class StressEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val stressLevel: Int,
    val sleepHours: Float,
    val mood: Int,
    val physicalActivityMinutes: Int,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
