package com.example.stress.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stress.R
import com.example.stress.domain.model.StressEntry
import java.time.LocalDate

@Composable
fun AddEntryScreen(
    onSave: (StressEntry) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var stressLevel by remember { mutableIntStateOf(5) }
    var sleepHours by remember { mutableFloatStateOf(7f) }
    var mood by remember { mutableIntStateOf(3) }
    var physicalActivity by remember { mutableIntStateOf(30) }
    var notes by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            title = { Text(stringResource(R.string.add_entry_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.nav_back))
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(stringResource(R.string.add_stress_level), style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Slider(
                        value = stressLevel.toFloat(),
                        onValueChange = { stressLevel = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stressLevel.toString(),
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.add_sleep_hours), style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
                Slider(
                    value = sleepHours,
                    onValueChange = { sleepHours = it },
                    valueRange = 0f..12f,
                    steps = 23,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.add_sleep_format, sleepHours),
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.add_mood), style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
                Slider(
                    value = mood.toFloat(),
                    onValueChange = { mood = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.add_physical_activity), style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
                Slider(
                    value = physicalActivity.toFloat(),
                    onValueChange = { physicalActivity = it.toInt() },
                    valueRange = 0f..120f,
                    steps = 23,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.add_minutes_format, physicalActivity),
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(stringResource(R.string.add_notes)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.add_cancel))
            }
            Button(
                onClick = {
                    onSave(
                        StressEntry(
                            id = 0,
                            date = LocalDate.now(),
                            stressLevel = stressLevel,
                            sleepHours = sleepHours,
                            mood = mood,
                            physicalActivityMinutes = physicalActivity,
                            notes = notes.ifBlank { null }
                        )
                    )
                    onBack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.add_save))
            }
        }
    }
}
