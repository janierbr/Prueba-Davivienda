package com.dashboard.tasks.domain.model

/**
 * Domain model for Dashboard metrics.
 * Calculated by GetDashboardMetricsUseCase (never calls API directly).
 */
data class DashboardMetrics(
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val inProgressTasks: Int,
    val tasksByUser: Map<String, Int>,
    val tasksByPriority: Map<String, Int>
)
