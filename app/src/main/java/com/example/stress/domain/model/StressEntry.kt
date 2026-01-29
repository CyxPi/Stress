package com.example.stress.domain.model

import java.time.LocalDate

/**
 * Domain model for a stress tracking entry.
 * Clean model used across domain and UI layers.
 */
data class StressEntry(
    val id: Long,
    val date: LocalDate,
    val stressLevel: Int,
    val sleepHours: Float,
    val mood: Int,
    val physicalActivityMinutes: Int,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
