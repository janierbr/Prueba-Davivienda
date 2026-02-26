package com.dashboard.tasks.domain.usecase

import com.dashboard.tasks.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String) = repository.deleteTask(taskId)
}
