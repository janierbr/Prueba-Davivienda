package com.dashboard.tasks.auth.data.repository

import android.content.SharedPreferences
import com.dashboard.tasks.auth.domain.repository.AuthRepository
import com.dashboard.tasks.data.remote.api.AuthApiService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val prefs: SharedPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> = runCatching {
        val response = api.login(mapOf("email" to email, "password" to password))
        val token = response.body()?.get("token") ?: error("Token no recibido")
        prefs.edit().putString("auth_token", token).apply()
        token
    }

    override suspend fun register(name: String, email: String, password: String): Result<String> =
        runCatching {
            api.register(mapOf("name" to name, "email" to email, "password" to password))
            login(email, password).getOrThrow()
        }

    override suspend fun logout() {
        runCatching { api.logout() }
        prefs.edit().remove("auth_token").apply()
    }

    override suspend fun refreshToken(): Result<String> = runCatching {
        val current = prefs.getString("auth_token", null) ?: error("Sin token")
        val response = api.refreshToken(mapOf("token" to current))
        val newToken = response.body()?.get("token") ?: error("Refresh fallido")
        prefs.edit().putString("auth_token", newToken).apply()
        newToken
    }

    override suspend fun isUserLoggedIn(): Boolean =
        prefs.getString("auth_token", null) != null
}
