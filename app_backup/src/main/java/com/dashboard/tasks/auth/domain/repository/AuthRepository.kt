package com.dashboard.tasks.auth.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(name: String, email: String, password: String): Result<String>
    suspend fun logout()
    suspend fun refreshToken(): Result<String>
    suspend fun isUserLoggedIn(): Boolean
}
