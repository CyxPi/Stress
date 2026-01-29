package com.example.stress.ui.navigation

/**
 * Navigation routes for the app.
 */
sealed class NavRoute(val route: String) {
    data object Dashboard : NavRoute("dashboard")
    data object AddEntry : NavRoute("add_entry")
    data object History : NavRoute("history")
    data object Relaxation : NavRoute("relaxation")
    data object BreathingExercise : NavRoute("breathing")
}
