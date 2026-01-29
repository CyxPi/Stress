package com.example.stress

import android.app.Application
import com.example.stress.data.local.database.StressDatabase
import com.example.stress.data.repository.StressRepository
import com.example.stress.domain.usecase.RelaxationRecommendationUseCase
import com.example.stress.domain.usecase.StressAnalysisUseCase

/**
 * Application class for dependency provision.
 * Provides singleton instances of database, repository, and use cases.
 *
 * TODO: Consider Hilt/Koin for production DI.
 */
class StressApplication : Application() {

    val database by lazy { StressDatabase.getInstance(this) }
    val stressRepository by lazy {
        StressRepository(database.stressEntryDao())
    }
    val stressAnalysisUseCase by lazy { StressAnalysisUseCase() }
    val relaxationRecommendationUseCase by lazy {
        RelaxationRecommendationUseCase(resources)
    }
}
