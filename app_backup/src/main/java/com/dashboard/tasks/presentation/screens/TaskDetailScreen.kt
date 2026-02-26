package com.dashboard.tasks.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.domain.model.TaskStatus
import com.dashboard.tasks.presentation.components.PriorityChip
import com.dashboard.tasks.presentation.components.StatusBadge
import com.dashboard.tasks.presentation.state.UiState
import com.dashboard.tasks.presentation.viewmodels.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    onBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasksState by viewModel.tasksState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadTasks() }

    val task = (tasksState as? UiState.Success)?.data?.find { it.id == taskId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (task != null) {
                        IconButton(onClick = {
                            viewModel.deleteTask(taskId)
                            onBack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                tasksState is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                task == null -> Text("Tarea no encontrada", modifier = Modifier.align(Alignment.Center))
                else -> TaskDetailContent(task = task, onStatusChange = { newStatus ->
                    viewModel.updateTaskStatus(task, newStatus)
                })
            }
        }
    }
}

@Composable
private fun TaskDetailContent(task: Task, onStatusChange: (TaskStatus) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(task.title, style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusBadge(task.status)
            PriorityChip(task.priority)
        }
        if (task.description.isNotBlank()) {
            Text(task.description, style = MaterialTheme.typography.bodyMedium)
        }

        Text("Cambiar estado", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TaskStatus.entries.forEach { status ->
                FilterChip(
                    selected = task.status == status,
                    onClick = { onStatusChange(status) },
                    label = { Text(status.name) }
                )
            }
        }
    }
}
