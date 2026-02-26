package com.dashboard.tasks.domain.usecase

import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): List<Task> = repository.getTasks()
}
