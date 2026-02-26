package com.dashboard.tasks.domain.model

/**
 * Domain entity representing a User.
 * Pure Kotlin – no framework dependencies.
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val avatarUrl: String = ""
)

enum class UserRole { ADMIN, MEMBER, VIEWER }
