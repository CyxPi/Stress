package com.example.stress.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stress.R
import com.example.stress.domain.model.StressTrend
import com.example.stress.ui.components.StressLevelIndicator
import com.example.stress.ui.viewmodel.StressViewModel

@Composable
fun DashboardScreen(
    viewModel: StressViewModel,
    onAddEntry: () -> Unit,
    onRelaxation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEntry,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.nav_add_entry))
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_title),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    IconButton(onClick = { viewModel.loadSampleData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.nav_load_sample))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Today's stress card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_today_stress),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (uiState.todayStressLevel > 0) {
                            StressLevelIndicator(level = uiState.todayStressLevel)
                            if (uiState.recommendations.isNotEmpty()) {
                                TextButton(onClick = onRelaxation) {
                                    Icon(Icons.Default.Spa, contentDescription = null, Modifier.size(18.dp))
                                    Spacer(Modifier.size(8.dp))
                                    Text(stringResource(R.string.dashboard_get_relaxation))
                                }
                            }
                        } else {
                            Text(
                                text = stringResource(R.string.dashboard_no_entry),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            TextButton(onClick = onAddEntry) {
                                Text(stringResource(R.string.dashboard_add_first))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Trend summary
                uiState.analysis?.let { analysis ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = stringResource(R.string.dashboard_trend_summary),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = stringResource(R.string.dashboard_average),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        text = String.format("%.1f", analysis.averageStress),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = stringResource(R.string.dashboard_trend),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Icon(
                                        imageVector = when (analysis.trend) {
                                            StressTrend.IMPROVING -> Icons.Default.TrendingDown
                                            StressTrend.WORSENING -> Icons.Default.TrendingUp
                                            else -> Icons.Default.TrendingDown
                                        },
                                        contentDescription = null,
                                        tint = when (analysis.trend) {
                                            StressTrend.IMPROVING -> MaterialTheme.colorScheme.primary
                                            StressTrend.WORSENING -> MaterialTheme.colorScheme.error
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                    Text(
                                        text = when (analysis.trend) {
                                            StressTrend.IMPROVING -> stringResource(R.string.dashboard_improving)
                                            StressTrend.WORSENING -> stringResource(R.string.dashboard_worsening)
                                            StressTrend.STABLE -> stringResource(R.string.dashboard_stable)
                                            StressTrend.INSUFFICIENT_DATA -> stringResource(R.string.dashboard_need_data)
                                        },
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = stringResource(R.string.dashboard_high_stress_days),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        text = analysis.highStressDays.toString(),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
