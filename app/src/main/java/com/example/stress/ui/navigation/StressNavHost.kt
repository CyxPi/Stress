package com.example.stress.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stress.StressApplication
import com.example.stress.domain.model.RelaxationRecommendation
import com.example.stress.domain.model.RelaxationType
import com.example.stress.ui.screens.AddEntryScreen
import com.example.stress.ui.screens.BreathingExerciseScreen
import com.example.stress.ui.screens.DashboardScreen
import com.example.stress.ui.screens.HistoryScreen
import com.example.stress.ui.screens.RelaxationScreen
import com.example.stress.ui.viewmodel.RelaxationViewModel
import com.example.stress.ui.viewmodel.StressViewModel

/**
 * Main navigation host for the app.
 * Defines all routes and screen compositions.
 */
@Composable
fun StressNavHost(
    app: StressApplication,
    navController: NavHostController = rememberNavController(),
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "StressNavHost must be used within a ViewModelStoreOwner context (e.g. Activity)"
    }
    val stressViewModel: StressViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return StressViewModel(
                    repository = app.stressRepository,
                    analysisUseCase = app.stressAnalysisUseCase,
                    recommendationUseCase = app.relaxationRecommendationUseCase,
                    resources = app.resources
                ) as T
            }
        }
    )

    var selectedBreathingExercise by remember { mutableStateOf<RelaxationRecommendation?>(null) }

    NavHost(
        navController = navController,
        startDestination = NavRoute.Dashboard.route,
        modifier = modifier
    ) {
        composable(NavRoute.Dashboard.route) {
            DashboardScreen(
                viewModel = stressViewModel,
                onAddEntry = { navController.navigate(NavRoute.AddEntry.route) },
                onRelaxation = {
                    val stressLevel = stressViewModel.uiState.value.todayStressLevel
                    navController.navigate(NavRoute.Relaxation.route)
                }
            )
        }

        composable(NavRoute.AddEntry.route) {
            AddEntryScreen(
                onSave = { entry ->
                    stressViewModel.addEntry(entry)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoute.History.route) {
            HistoryScreen(viewModel = stressViewModel)
        }

        composable(NavRoute.Relaxation.route) {
            val stressLevel = stressViewModel.uiState.value.todayStressLevel.coerceIn(1, 10)
            val relaxationViewModel = remember(stressLevel) {
                RelaxationViewModel(
                    recommendationUseCase = app.relaxationRecommendationUseCase,
                    stressLevel = if (stressLevel > 0) stressLevel else 5
                )
            }
            RelaxationScreen(
                viewModel = relaxationViewModel,
                stressLevel = if (stressLevel > 0) stressLevel else 5,
                onBreathingExercise = { recommendation ->
                    selectedBreathingExercise = recommendation
                    navController.navigate(NavRoute.BreathingExercise.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoute.BreathingExercise.route) {
            val defaultExercise = app.relaxationRecommendationUseCase.getRecommendations(5)
                .first { it.type == RelaxationType.BREATHING }
            BreathingExerciseScreen(
                exercise = selectedBreathingExercise ?: defaultExercise,
                onBack = {
                    selectedBreathingExercise = null
                    navController.popBackStack()
                }
            )
        }
    }
}
