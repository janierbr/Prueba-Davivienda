package com.dashboard.tasks.domain.repository

import com.dashboard.tasks.domain.model.User

/**
 * Contract for User data operations.
 */
interface UserRepository {
    suspend fun getUserById(id: String): User?
    suspend fun getUsersByProject(projectId: String): List<User>
    suspend fun getCurrentUser(): User?
    suspend fun updateUser(user: User)
}
