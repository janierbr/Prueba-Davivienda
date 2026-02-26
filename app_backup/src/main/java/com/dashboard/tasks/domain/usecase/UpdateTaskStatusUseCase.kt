package com.dashboard.tasks.domain.usecase

import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.domain.model.TaskStatus
import com.dashboard.tasks.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskStatusUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task, newStatus: TaskStatus) {
        repository.updateTask(task.copy(status = newStatus))
    }
}
