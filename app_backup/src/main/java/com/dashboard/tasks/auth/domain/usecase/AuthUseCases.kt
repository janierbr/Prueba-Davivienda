package com.dashboard.tasks.auth.domain.usecase

import com.dashboard.tasks.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<String> =
        repository.login(email, password)
}

class LogoutUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class IsUserLoggedInUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean = repository.isUserLoggedIn()
}
