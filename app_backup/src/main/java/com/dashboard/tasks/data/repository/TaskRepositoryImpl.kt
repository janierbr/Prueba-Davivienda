package com.dashboard.tasks.data.repository

import com.dashboard.tasks.data.local.database.TaskDao
import com.dashboard.tasks.data.mapper.TaskMapper
import com.dashboard.tasks.data.remote.api.TaskApiService
import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApiService,
    private val dao: TaskDao,
    private val mapper: TaskMapper
) : TaskRepository {

    override suspend fun getTasks(): List<Task> {
        return try {
            // 1. Fetch from remote
            val response = api.getTasks()
            if (response.isSuccessful) {
                val tasks = response.body()?.map { mapper.dtoToDomain(it) } ?: emptyList()
                // 2. Update local cache
                dao.insertTasks(tasks.map { mapper.domainToEntity(it) })
                tasks
            } else {
                // 3. Fallback to local cache
                dao.getAllTasks().map { mapper.entityToDomain(it) }
            }
        } catch (e: Exception) {
            // 4. Offline fallback
            dao.getAllTasks().map { mapper.entityToDomain(it) }
        }
    }

    override suspend fun getTaskById(id: String): Task? {
        return dao.getTaskById(id)?.let { mapper.entityToDomain(it) }
    }

    override suspend fun getTasksByUser(userId: String): List<Task> {
        return dao.getTasksByUser(userId).map { mapper.entityToDomain(it) }
    }

    override suspend fun getTasksByProject(projectId: String): List<Task> {
        return dao.getTasksByProject(projectId).map { mapper.entityToDomain(it) }
    }

    override suspend fun createTask(task: Task) {
        val dto = mapper.domainToDto(task)
        api.createTask(dto)
        dao.insertTask(mapper.domainToEntity(task))
    }

    override suspend fun updateTask(task: Task) {
        val dto = mapper.domainToDto(task)
        api.updateTask(task.id, dto)
        dao.updateTask(mapper.domainToEntity(task))
    }

    override suspend fun deleteTask(id: String) {
        api.deleteTask(id)
        dao.deleteTask(id)
    }
}
