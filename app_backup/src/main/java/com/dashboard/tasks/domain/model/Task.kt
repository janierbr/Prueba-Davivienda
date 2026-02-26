package com.dashboard.tasks.domain.model

/**
 * Domain entity representing a Task.
 * Pure Kotlin – no framework dependencies.
 */
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val priority: TaskPriority,
    val assignedUserId: String,
    val projectId: String,
    val createdAt: Long,
    val dueDate: Long
)

enum class TaskStatus { TODO, IN_PROGRESS, DONE }
enum class TaskPriority { LOW, MEDIUM, HIGH }
