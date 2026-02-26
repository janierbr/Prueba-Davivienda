package com.dashboard.tasks.data.mapper

import com.dashboard.tasks.data.local.entity.TaskEntity
import com.dashboard.tasks.data.remote.dto.TaskDto
import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.domain.model.TaskPriority
import com.dashboard.tasks.domain.model.TaskStatus
import javax.inject.Inject

class TaskMapper @Inject constructor() {

    fun dtoToDomain(dto: TaskDto): Task = Task(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        status = TaskStatus.valueOf(dto.status),
        priority = TaskPriority.valueOf(dto.priority),
        assignedUserId = dto.assignedUserId,
        projectId = dto.projectId,
        createdAt = dto.createdAt,
        dueDate = dto.dueDate
    )

    fun domainToDto(domain: Task): TaskDto = TaskDto(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        status = domain.status.name,
        priority = domain.priority.name,
        assignedUserId = domain.assignedUserId,
        projectId = domain.projectId,
        createdAt = domain.createdAt,
        dueDate = domain.dueDate
    )

    fun entityToDomain(entity: TaskEntity): Task = Task(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        status = TaskStatus.valueOf(entity.status),
        priority = TaskPriority.valueOf(entity.priority),
        assignedUserId = entity.assignedUserId,
        projectId = entity.projectId,
        createdAt = entity.createdAt,
        dueDate = entity.dueDate
    )

    fun domainToEntity(domain: Task): TaskEntity = TaskEntity(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        status = domain.status.name,
        priority = domain.priority.name,
        assignedUserId = domain.assignedUserId,
        projectId = domain.projectId,
        createdAt = domain.createdAt,
        dueDate = domain.dueDate
    )
}
