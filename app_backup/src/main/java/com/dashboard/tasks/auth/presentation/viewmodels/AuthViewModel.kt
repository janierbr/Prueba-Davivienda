package com.dashboard.tasks.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashboard.tasks.auth.domain.usecase.LoginUseCase
import com.dashboard.tasks.auth.domain.usecase.LogoutUseCase
import com.dashboard.tasks.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val authState: StateFlow<UiState<String>> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            loginUseCase(email, password)
                .onSuccess { token -> _authState.value = UiState.Success(token) }
                .onFailure { e -> _authState.value = UiState.Error(e.message ?: "Login fallido") }
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }
}
