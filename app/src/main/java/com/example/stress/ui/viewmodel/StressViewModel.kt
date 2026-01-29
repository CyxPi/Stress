package com.example.stress.ui.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stress.R
import com.example.stress.domain.model.RelaxationRecommendation
import com.example.stress.domain.model.StressAnalysis
import com.example.stress.domain.model.StressEntry
import com.example.stress.domain.usecase.StressAnalysisUseCase
import com.example.stress.domain.usecase.RelaxationRecommendationUseCase
import com.example.stress.data.repository.StressRepository
import com.example.stress.data.local.SampleDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel for stress tracking screens.
 * Uses StateFlow for reactive UI updates.
 */
class StressViewModel(
    private val repository: StressRepository,
    private val analysisUseCase: StressAnalysisUseCase,
    private val recommendationUseCase: RelaxationRecommendationUseCase,
    private val resources: Resources
) : ViewModel() {

    private val _uiState = MutableStateFlow(StressUiState())
    val uiState: StateFlow<StressUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getRecentEntries(30)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message ?: resources.getString(R.string.error_unknown)) }
                }
                .collect { entries ->
                    val analysis = analysisUseCase.analyze(entries)
                    val todayEntries = entries.filter { it.date == LocalDate.now() }
                    val todayStress = todayEntries.maxOfOrNull { it.stressLevel } ?: 0
                    val recommendations = recommendationUseCase.getRecommendations(todayStress)

                    _uiState.update {
                        it.copy(
                            entries = entries,
                            analysis = analysis,
                            todayStressLevel = todayStress,
                            recommendations = recommendations,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun addEntry(entry: StressEntry) {
        viewModelScope.launch {
            try {
                repository.insertEntry(entry)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: resources.getString(R.string.error_save_failed)) }
            }
        }
    }

    fun loadSampleData() {
        viewModelScope.launch {
            try {
                val sampleEntries = SampleDataProvider.generateSampleEntries(14)
                repository.insertEntries(sampleEntries)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: resources.getString(R.string.error_load_sample_failed)) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteEntry(id)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: resources.getString(R.string.error_delete_failed)) }
            }
        }
    }
}

data class StressUiState(
    val entries: List<StressEntry> = emptyList(),
    val analysis: StressAnalysis? = null,
    val todayStressLevel: Int = 0,
    val recommendations: List<RelaxationRecommendation> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
