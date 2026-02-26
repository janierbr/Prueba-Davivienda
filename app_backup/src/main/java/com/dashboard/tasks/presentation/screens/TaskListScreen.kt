package com.dashboard.tasks.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.presentation.components.TaskCard
import com.dashboard.tasks.presentation.state.UiState
import com.dashboard.tasks.presentation.viewmodels.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onTaskClick: (String) -> Unit,
    onCreateClick: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasksState by viewModel.tasksState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadTasks() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(Icons.Default.Add, contentDescription = "Crear tarea")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = tasksState) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text(
                            text = "No hay tareas aún. ¡Crea una!",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.data, key = { it.id }) { task ->
                                TaskCard(task = task, onClick = { onTaskClick(task.id) })
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}
