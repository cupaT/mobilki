package com.example.bookbrowser.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookbrowser.config.BuildConfigHelper
import com.example.bookbrowser.config.DebugSettingsStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugSettingsScreen(
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf(DebugSettingsStore.bookQuery.value) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Debug Settings") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("This screen is available only in debug builds.")
            Text("Flavor: ${BuildConfigHelper.flavorName}")
            Text("Build type: ${BuildConfigHelper.buildLabel}")
            Text("API: ${BuildConfigHelper.apiBaseUrl}")

            HorizontalDivider()

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Book query") },
                supportingText = {
                    Text("Example: subject:history or android development")
                }
            )

            Button(
                onClick = {
                    DebugSettingsStore.updateBookQuery(query)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply")
            }

            TextButton(
                onClick = {
                    DebugSettingsStore.reset()
                    query = DebugSettingsStore.bookQuery.value
                }
            ) {
                Text("Reset to default")
            }
        }
    }
}
