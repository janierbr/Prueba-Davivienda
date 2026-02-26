package com.dashboard.tasks.data.repository

import com.dashboard.tasks.data.local.database.UserDao
import com.dashboard.tasks.data.local.entity.UserEntity
import com.dashboard.tasks.data.mapper.TaskMapper
import com.dashboard.tasks.data.remote.api.AuthApiService
import com.dashboard.tasks.domain.model.User
import com.dashboard.tasks.domain.model.UserRole
import com.dashboard.tasks.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val dao: UserDao
) : UserRepository {

    override suspend fun getUserById(id: String): User? {
        return try {
            val response = api.getCurrentUser()
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    val entity = UserEntity(
                        id        = dto.id,
                        name      = dto.name,
                        email     = dto.email,
                        role      = dto.role,
                        avatarUrl = dto.avatarUrl
                    )
                    dao.insertUser(entity)
                    entity.toDomain()
                }
            } else {
                dao.getUserById(id)?.toDomain()
            }
        } catch (e: Exception) {
            dao.getUserById(id)?.toDomain()
        }
    }

    override suspend fun getUsersByProject(projectId: String): List<User> = emptyList()

    override suspend fun getCurrentUser(): User? = getUserById("")

    override suspend fun updateUser(user: User) {
        dao.updateUser(
            UserEntity(
                id        = user.id,
                name      = user.name,
                email     = user.email,
                role      = user.role.name,
                avatarUrl = user.avatarUrl
            )
        )
    }

    private fun UserEntity.toDomain(): User = User(
        id        = id,
        name      = name,
        email     = email,
        role      = UserRole.valueOf(role),
        avatarUrl = avatarUrl
    )
}
