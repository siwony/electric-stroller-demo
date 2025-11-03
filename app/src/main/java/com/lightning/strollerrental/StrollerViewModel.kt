package com.lightning.strollerrental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RentalUiState(
    val currentState: RentalState = RentalState.IDLE,
    val currentStep: Int = 0,
    val stepDelayMs: Long = 10000L,
    val forceFail: Boolean = false
)

class StrollerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RentalUiState())
    val uiState: StateFlow<RentalUiState> = _uiState.asStateFlow()

    fun updateStepDelay(delayMs: Long) {
        _uiState.value = _uiState.value.copy(stepDelayMs = delayMs)
    }

    fun toggleForceFail() {
        _uiState.value = _uiState.value.copy(forceFail = !_uiState.value.forceFail)
    }

    fun startRenting() {
        viewModelScope.launch {
            val delayMs = _uiState.value.stepDelayMs
            val shouldFail = _uiState.value.forceFail
            
            _uiState.value = _uiState.value.copy(
                currentState = RentalState.RENTING,
                currentStep = 0
            )
            
            // Step 1: 유모차와 연결 중...
            delay(delayMs)
            if (shouldFail) {
                _uiState.value = RentalUiState(
                    stepDelayMs = _uiState.value.stepDelayMs,
                    forceFail = _uiState.value.forceFail
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 1)
            
            // Step 2: 사용자 인증 중...
            delay(delayMs)
            if (shouldFail) {
                _uiState.value = RentalUiState(
                    stepDelayMs = _uiState.value.stepDelayMs,
                    forceFail = _uiState.value.forceFail
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 2)
            
            // Step 3: 잠금 장치 해제 중...
            delay(delayMs)
            if (shouldFail) {
                _uiState.value = RentalUiState(
                    stepDelayMs = _uiState.value.stepDelayMs,
                    forceFail = _uiState.value.forceFail
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 3)
            
            // Step 4: 대여 완료!
            delay(delayMs)
            _uiState.value = _uiState.value.copy(
                currentState = RentalState.RENTED,
                currentStep = 0
            )
        }
    }

    fun startReturning() {
        viewModelScope.launch {
            val delayMs = _uiState.value.stepDelayMs
            val shouldFail = _uiState.value.forceFail
            
            _uiState.value = _uiState.value.copy(currentState = RentalState.RETURNING)
            delay(delayMs)
            
            if (shouldFail) {
                _uiState.value = _uiState.value.copy(currentState = RentalState.RENTED)
                return@launch
            }
            
            _uiState.value = _uiState.value.copy(currentState = RentalState.RETURNED)
        }
    }

    fun reset() {
        _uiState.value = RentalUiState(
            stepDelayMs = _uiState.value.stepDelayMs,
            forceFail = _uiState.value.forceFail
        )
    }
}
