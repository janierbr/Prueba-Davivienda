package com.dashboard.tasks.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashboard.tasks.domain.model.DashboardMetrics
import com.dashboard.tasks.presentation.state.UiState
import com.dashboard.tasks.presentation.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val metricsState by viewModel.metricsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (val state = metricsState) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is UiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> MetricsGrid(state.data)
                else -> Unit
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = onNavigateToTasks, modifier = Modifier.fillMaxWidth()) {
                Text("Ver todas las tareas")
            }
            OutlinedButton(onClick = onNavigateToCreate, modifier = Modifier.fillMaxWidth()) {
                Text("Crear nueva tarea")
            }
        }
    }
}

@Composable
private fun MetricsGrid(metrics: DashboardMetrics) {
    val items = listOf(
        "Total" to metrics.totalTasks,
        "Completadas" to metrics.completedTasks,
        "En progreso" to metrics.inProgressTasks,
        "Pendientes" to metrics.pendingTasks
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Resumen", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { (label, count) ->
                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = count.toString(), style = MaterialTheme.typography.headlineMedium)
                        Text(text = label, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
