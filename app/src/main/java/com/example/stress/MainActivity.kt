package com.example.stress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stress.R
import com.example.stress.ui.navigation.NavRoute
import com.example.stress.ui.navigation.StressNavHost
import com.example.stress.ui.theme.StressTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as StressApplication
        setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides this) {
                StressTheme {
                    StressApp(app = app)
                }
            }
        }
    }
}

@Composable
fun StressApp(app: StressApplication) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in listOf(
        NavRoute.Dashboard.route,
        NavRoute.History.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == NavRoute.Dashboard.route } == true,
                        onClick = {
                            navController.navigate(NavRoute.Dashboard.route) {
                                popUpTo(NavRoute.Dashboard.route) {
                                    saveState = true
                                    inclusive = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.nav_dashboard)) },
                        label = { Text(stringResource(R.string.nav_dashboard)) }
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == NavRoute.History.route } == true,
                        onClick = {
                            navController.navigate(NavRoute.History.route) {
                                popUpTo(NavRoute.Dashboard.route) {
                                    saveState = true
                                    inclusive = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.ShowChart, contentDescription = stringResource(R.string.nav_history)) },
                        label = { Text(stringResource(R.string.nav_history)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        StressNavHost(
            app = app,
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
