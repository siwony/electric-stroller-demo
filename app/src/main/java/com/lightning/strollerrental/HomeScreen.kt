package com.lightning.strollerrental

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    uiState: RentalUiState,
    onStartRenting: () -> Unit,
    onUpdateDelay: (Long) -> Unit,
    onToggleForceFail: () -> Unit
) {
    var delayInput by remember { mutableStateOf(uiState.stepDelayMs.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "전기 유모차 대여",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartRenting,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("대여 시작", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Debug Panel
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Debug Panel",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = delayInput,
                    onValueChange = { newValue ->
                        delayInput = newValue
                        newValue.toLongOrNull()?.let { delay ->
                            if (delay > 0) {
                                onUpdateDelay(delay)
                            }
                        }
                    },
                    label = { Text("Step Delay (ms)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Force Fail")
                    Switch(
                        checked = uiState.forceFail,
                        onCheckedChange = { onToggleForceFail() }
                    )
                }
            }
        }
    }
}
