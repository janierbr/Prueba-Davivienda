package com.dashboard.tasks.domain.usecase

import com.dashboard.tasks.domain.model.DashboardMetrics
import com.dashboard.tasks.domain.model.TaskPriority
import com.dashboard.tasks.domain.model.TaskStatus
import com.dashboard.tasks.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Calculates dashboard metrics from the task list.
 * Does NOT call the API directly – uses the repository abstraction.
 */
class GetDashboardMetricsUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): DashboardMetrics {
        val tasks = repository.getTasks()

        val byUser = tasks.groupBy { it.assignedUserId }
            .mapValues { it.value.size }

        val byPriority = tasks.groupBy { it.priority.name }
            .mapValues { it.value.size }

        return DashboardMetrics(
            totalTasks = tasks.size,
            completedTasks = tasks.count { it.status == TaskStatus.DONE },
            pendingTasks = tasks.count { it.status == TaskStatus.TODO },
            inProgressTasks = tasks.count { it.status == TaskStatus.IN_PROGRESS },
            tasksByUser = byUser,
            tasksByPriority = byPriority
        )
    }
}
