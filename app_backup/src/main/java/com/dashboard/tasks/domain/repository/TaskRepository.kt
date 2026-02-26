package com.dashboard.tasks.domain.repository

import com.dashboard.tasks.domain.model.Task

/**
 * Contract for Task data operations.
 * Only interfaces here – no implementation details.
 */
interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun getTaskById(id: String): Task?
    suspend fun getTasksByUser(userId: String): List<Task>
    suspend fun getTasksByProject(projectId: String): List<Task>
    suspend fun createTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: String)
}
