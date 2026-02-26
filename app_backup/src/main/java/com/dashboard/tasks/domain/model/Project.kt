package com.dashboard.tasks.domain.model

/**
 * Domain entity representing a Project.
 */
data class Project(
    val id: String,
    val name: String,
    val description: String,
    val memberIds: List<String>,
    val createdAt: Long
)
