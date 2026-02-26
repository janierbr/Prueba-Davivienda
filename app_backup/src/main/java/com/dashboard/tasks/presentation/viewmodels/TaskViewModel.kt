package com.dashboard.tasks.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.domain.model.TaskStatus
import com.dashboard.tasks.domain.usecase.CreateTaskUseCase
import com.dashboard.tasks.domain.usecase.DeleteTaskUseCase
import com.dashboard.tasks.domain.usecase.GetTasksUseCase
import com.dashboard.tasks.domain.usecase.UpdateTaskStatusUseCase
import com.dashboard.tasks.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _tasksState = MutableStateFlow<UiState<List<Task>>>(UiState.Idle)
    val tasksState: StateFlow<UiState<List<Task>>> = _tasksState

    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState: StateFlow<UiState<Unit>> = _actionState

    fun loadTasks() {
        viewModelScope.launch {
            _tasksState.value = UiState.Loading
            try {
                val tasks = getTasksUseCase()
                _tasksState.value = UiState.Success(tasks)
            } catch (e: Exception) {
                _tasksState.value = UiState.Error(e.message ?: "Error loading tasks")
            }
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            _actionState.value = UiState.Loading
            try {
                createTaskUseCase(task)
                _actionState.value = UiState.Success(Unit)
                loadTasks()
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Error creating task")
            }
        }
    }

    fun updateTaskStatus(task: Task, newStatus: TaskStatus) {
        viewModelScope.launch {
            try {
                updateTaskStatusUseCase(task, newStatus)
                loadTasks()
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Error updating task")
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId)
                loadTasks()
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Error deleting task")
            }
        }
    }
}
