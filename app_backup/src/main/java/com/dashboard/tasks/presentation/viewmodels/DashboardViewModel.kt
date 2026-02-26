package com.dashboard.tasks.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashboard.tasks.domain.model.DashboardMetrics
import com.dashboard.tasks.domain.usecase.GetDashboardMetricsUseCase
import com.dashboard.tasks.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardMetricsUseCase: GetDashboardMetricsUseCase
) : ViewModel() {

    private val _metricsState = MutableStateFlow<UiState<DashboardMetrics>>(UiState.Idle)
    val metricsState: StateFlow<UiState<DashboardMetrics>> = _metricsState

    init {
        loadMetrics()
    }

    fun loadMetrics() {
        viewModelScope.launch {
            _metricsState.value = UiState.Loading
            try {
                val metrics = getDashboardMetricsUseCase()
                _metricsState.value = UiState.Success(metrics)
            } catch (e: Exception) {
                _metricsState.value = UiState.Error(e.message ?: "Error loading metrics")
            }
        }
    }
}
