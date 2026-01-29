package com.example.stress.domain.model

/**
 * A single relaxation recommendation for the user.
 */
data class RelaxationRecommendation(
    val title: String,
    val description: String,
    val type: RelaxationType,
    val durationMinutes: Int,
    val priority: Int
)

enum class RelaxationType {
    BREATHING,
    MINDFULNESS,
    TECHNIQUE
}
