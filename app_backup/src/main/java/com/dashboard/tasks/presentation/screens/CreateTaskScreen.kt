package com.dashboard.tasks.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.domain.model.TaskPriority
import com.dashboard.tasks.domain.model.TaskStatus
import com.dashboard.tasks.presentation.state.UiState
import com.dashboard.tasks.presentation.viewmodels.TaskViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onTaskCreated: () -> Unit,
    onBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val actionState by viewModel.actionState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }

    LaunchedEffect(actionState) {
        if (actionState is UiState.Success) onTaskCreated()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Text("Prioridad", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TaskPriority.entries.forEach { priority ->
                    FilterChip(
                        selected = selectedPriority == priority,
                        onClick = { selectedPriority = priority },
                        label = { Text(priority.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (actionState is UiState.Error) {
                Text((actionState as UiState.Error).message, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.createTask(
                            Task(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                description = description,
                                status = TaskStatus.TODO,
                                priority = selectedPriority,
                                assignedUserId = "",
                                projectId = "",
                                createdAt = System.currentTimeMillis(),
                                dueDate = System.currentTimeMillis()
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && actionState !is UiState.Loading
            ) {
                if (actionState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Crear Tarea")
                }
            }
        }
    }
}
