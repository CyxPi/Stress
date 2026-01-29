package com.example.stress.domain.usecase

import android.content.res.Resources
import com.example.stress.R
import com.example.stress.domain.model.RelaxationRecommendation
import com.example.stress.domain.model.RelaxationType

/**
 * Provides relaxation recommendations based on stress level.
 * Uses Resources for localized strings.
 *
 * TODO: ML integration - can be enhanced with personalized
 * recommendations based on user history and effectiveness.
 */
class RelaxationRecommendationUseCase(
    private val resources: Resources
) {

    /**
     * Gets recommendations tailored to current stress level.
     * @param stressLevel 1-10 scale
     * @return List of prioritized recommendations
     */
    fun getRecommendations(stressLevel: Int): List<RelaxationRecommendation> {
        val level = stressLevel.coerceIn(1, 10)
        return when {
            level >= 8 -> getHighStressRecommendations()
            level >= 5 -> getMediumStressRecommendations()
            else -> getLowStressRecommendations()
        }
    }

    private fun getHighStressRecommendations(): List<RelaxationRecommendation> {
        return listOf(
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_4_7_8_title),
                description = resources.getString(R.string.rec_4_7_8_desc),
                type = RelaxationType.BREATHING,
                durationMinutes = 2,
                priority = 1
            ),
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_grounding_title),
                description = resources.getString(R.string.rec_grounding_desc),
                type = RelaxationType.MINDFULNESS,
                durationMinutes = 3,
                priority = 2
            ),
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_pmr_title),
                description = resources.getString(R.string.rec_pmr_desc),
                type = RelaxationType.TECHNIQUE,
                durationMinutes = 10,
                priority = 3
            ),
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_break_title),
                description = resources.getString(R.string.rec_break_desc),
                type = RelaxationType.MINDFULNESS,
                durationMinutes = 5,
                priority = 4
            )
        )
    }

    private fun getMediumStressRecommendations(): List<RelaxationRecommendation> {
        return listOf(
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_box_title),
                description = resources.getString(R.string.rec_box_desc),
                type = RelaxationType.BREATHING,
                durationMinutes = 2,
                priority = 1
            ),
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_mindful_title),
                description = resources.getString(R.string.rec_mindful_desc),
                type = RelaxationType.MINDFULNESS,
                durationMinutes = 3,
                priority = 2
            ),
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_stretch_title),
                description = resources.getString(R.string.rec_stretch_desc),
                type = RelaxationType.TECHNIQUE,
                durationMinutes = 5,
                priority = 3
            )
        )
    }

    private fun getLowStressRecommendations(): List<RelaxationRecommendation> {
        return listOf(
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_deep_title),
                description = resources.getString(R.string.rec_deep_desc),
                type = RelaxationType.BREATHING,
                durationMinutes = 1,
                priority = 1
            ),
            RelaxationRecommendation(
                title = resources.getString(R.string.rec_gratitude_title),
                description = resources.getString(R.string.rec_gratitude_desc),
                type = RelaxationType.MINDFULNESS,
                durationMinutes = 2,
                priority = 2
            )
        )
    }

    /**
     * Returns mindfulness tips for display in relaxation screen.
     */
    fun getMindfulnessTips(): List<String> = listOf(
        resources.getString(R.string.tip_1),
        resources.getString(R.string.tip_2),
        resources.getString(R.string.tip_3),
        resources.getString(R.string.tip_4),
        resources.getString(R.string.tip_5),
        resources.getString(R.string.tip_6),
        resources.getString(R.string.tip_7)
    )
}
