package com.example.stress.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.stress.domain.model.RelaxationRecommendation
import com.example.stress.domain.usecase.RelaxationRecommendationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for relaxation screen.
 * Provides recommendations and mindfulness tips.
 */
class RelaxationViewModel(
    private val recommendationUseCase: RelaxationRecommendationUseCase,
    stressLevel: Int = 5
) : ViewModel() {

    private val _uiState = MutableStateFlow(RelaxationUiState())
    val uiState: StateFlow<RelaxationUiState> = _uiState.asStateFlow()

    init {
        loadRecommendations(stressLevel)
    }

    fun loadRecommendations(stressLevel: Int) {
        val recommendations = recommendationUseCase.getRecommendations(stressLevel)
        val tips = recommendationUseCase.getMindfulnessTips()
        _uiState.update {
            it.copy(
                recommendations = recommendations,
                mindfulnessTips = tips,
                selectedStressLevel = stressLevel
            )
        }
    }

    fun selectBreathingExercise(exercise: RelaxationRecommendation?) {
        _uiState.update { it.copy(selectedBreathingExercise = exercise) }
    }

    fun clearSelectedExercise() {
        _uiState.update { it.copy(selectedBreathingExercise = null) }
    }
}

data class RelaxationUiState(
    val recommendations: List<RelaxationRecommendation> = emptyList(),
    val mindfulnessTips: List<String> = emptyList(),
    val selectedStressLevel: Int = 5,
    val selectedBreathingExercise: RelaxationRecommendation? = null
)
