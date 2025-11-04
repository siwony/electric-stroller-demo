package com.lightning.strollerrental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import java.util.UUID

data class RentalUiState(
    val currentState: RentalState = RentalState.IDLE,
    val currentStep: Int = 0,
    val stepDelayMs: Long = 10000L,
    val forceFail: Boolean = false,
    val strollerId: String? = null,
    val startAt: Long? = null,
    val elapsedSec: Int = 0,
    val errorMessage: String? = null,
    val showSplash: Boolean = true
)

class StrollerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RentalUiState())
    val uiState: StateFlow<RentalUiState> = _uiState.asStateFlow()
    
    private var timerJob: Job? = null

    fun dismissSplash() {
        _uiState.value = _uiState.value.copy(showSplash = false)
    }

    fun updateStepDelay(delayMs: Long) {
        _uiState.value = _uiState.value.copy(stepDelayMs = delayMs)
    }

    fun toggleForceFail() {
        _uiState.value = _uiState.value.copy(forceFail = !_uiState.value.forceFail)
    }

    private fun generateStrollerId(): String {
        val timestamp = System.currentTimeMillis() % 10000
        return "ST$timestamp"
    }

    private fun startElapsedTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val startTime = _uiState.value.startAt ?: return@launch
            while (true) {
                delay(1000L) // 1 second
                val elapsed = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                _uiState.value = _uiState.value.copy(elapsedSec = elapsed)
            }
        }
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
                _uiState.value = _uiState.value.copy(
                    currentState = RentalState.ERROR,
                    errorMessage = "연결 실패. 다시 시도해주세요."
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 1)
            
            // Step 2: 사용자 인증 중...
            delay(delayMs)
            if (shouldFail) {
                _uiState.value = _uiState.value.copy(
                    currentState = RentalState.ERROR,
                    errorMessage = "인증 실패. 다시 시도해주세요."
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 2)
            
            // Step 3: 잠금 장치 해제 중...
            delay(delayMs)
            if (shouldFail) {
                _uiState.value = _uiState.value.copy(
                    currentState = RentalState.ERROR,
                    errorMessage = "잠금 해제 실패. 다시 시도해주세요."
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 3)
            
            // Step 4: 대여 완료!
            delay(delayMs)
            val strollerId = generateStrollerId()
            val startTime = System.currentTimeMillis()
            _uiState.value = _uiState.value.copy(
                currentState = RentalState.RENTED,
                currentStep = 0,
                strollerId = strollerId,
                startAt = startTime,
                elapsedSec = 0
            )
            startElapsedTimer()
        }
    }

    fun startReturning() {
        timerJob?.cancel()
        viewModelScope.launch {
            val delayMs = _uiState.value.stepDelayMs
            val shouldFail = _uiState.value.forceFail
            
            _uiState.value = _uiState.value.copy(
                currentState = RentalState.RETURNING,
                currentStep = 0
            )
            
            // Step 1: 유모차와 연결 중...
            delay(delayMs)
            if (shouldFail) {
                _uiState.value = _uiState.value.copy(
                    currentState = RentalState.ERROR,
                    errorMessage = "연결 실패. 다시 시도해주세요."
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 1)
            
            // Step 2: 유모차 상태 확인 중...
            delay(delayMs)
            if (shouldFail) {
                _uiState.value = _uiState.value.copy(
                    currentState = RentalState.ERROR,
                    errorMessage = "상태 확인 실패. 다시 시도해주세요."
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 2)
            
            // Step 3: 잠금 장치 잠그는 중...
            delay(delayMs)
            if (shouldFail) {
                _uiState.value = _uiState.value.copy(
                    currentState = RentalState.ERROR,
                    errorMessage = "잠금 실패. 다시 시도해주세요."
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentStep = 3)
            
            // Step 4: 반납 완료!
            delay(delayMs)
            _uiState.value = _uiState.value.copy(
                currentState = RentalState.RETURNED,
                currentStep = 0
            )
        }
    }

    fun retryAfterError() {
        val previousState = when (_uiState.value.errorMessage) {
            null -> RentalState.IDLE
            else -> if (_uiState.value.strollerId != null) RentalState.RENTED else RentalState.IDLE
        }
        
        _uiState.value = _uiState.value.copy(
            currentState = previousState,
            errorMessage = null
        )
        
        // Restart timer if returning to rented state
        if (previousState == RentalState.RENTED) {
            startElapsedTimer()
        }
    }

    fun reset() {
        timerJob?.cancel()
        _uiState.value = RentalUiState(
            stepDelayMs = _uiState.value.stepDelayMs,
            forceFail = _uiState.value.forceFail,
            showSplash = false
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
