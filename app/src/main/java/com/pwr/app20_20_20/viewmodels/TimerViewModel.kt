package com.pwr.app20_20_20.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.ceil

enum class TimerMode {
    Focus, Rest
}
data class Time(val minutes: Int, val seconds: Int)

class TimerViewModel : ViewModel() {
    private val _numberOfCycles = MutableStateFlow(1)
    val numberOfCycles: StateFlow<Int> = _numberOfCycles.asStateFlow()

    private val _focusTime = MutableStateFlow(Time(0, 5))
    val focusTime: StateFlow<Time> = _focusTime.asStateFlow()

    private val _restTime = MutableStateFlow(Time(0, 3))
    val restTime: StateFlow<Time> = _restTime.asStateFlow()

    private val _currentTime = MutableStateFlow(0L)
    val currentTime: StateFlow<Long> = _currentTime.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _mode = MutableStateFlow(TimerMode.Focus)
    val mode: StateFlow<TimerMode> = _mode.asStateFlow()

    private val _currentCycle = MutableStateFlow(0)
    val currentCycle: StateFlow<Int> = _currentCycle.asStateFlow()

    val cycleRange = 1..10
    val timeRange = 0..59 // Assuming you want a range of 0 to 59 for both minutes and seconds

    init {
        resetTimer()
    }

    fun setNumberOfCycles(cycles: Int) {
        if (cycles in cycleRange) {
            viewModelScope.launch {
                _numberOfCycles.value = cycles
                resetTimer()
            }
        }
    }

    fun setFocusTimeMinutes(minutes: Int) {
        if (minutes in timeRange) {
            viewModelScope.launch {
                _focusTime.value = _focusTime.value.copy(minutes = minutes)
                resetTimer()
            }
        }
    }

    fun setFocusTimeSeconds(seconds: Int) {
        if (seconds in timeRange) {
            viewModelScope.launch {
                _focusTime.value = _focusTime.value.copy(seconds = seconds)
                resetTimer()
            }
        }
    }

    fun setRestTimeMinutes(minutes: Int) {
        if (minutes in timeRange) {
            viewModelScope.launch {
                _restTime.value = _restTime.value.copy(minutes = minutes)
                resetTimer()
            }
        }
    }

    fun setRestTimeSeconds(seconds: Int) {
        if (seconds in timeRange) {
            viewModelScope.launch {
                _restTime.value = _restTime.value.copy(seconds = seconds)
                resetTimer()
            }
        }
    }

    fun calculateMillis(time: Time): Long {
        return (time.minutes * 60 + time.seconds) * 1000L
    }

    fun startTimer() {
        if (!_isTimerRunning.value) {
            _isTimerRunning.value = true
            viewModelScope.launch {
                runTimer()
            }
        }
    }

    fun stopTimer() {
        _isTimerRunning.value = false
    }

    fun resetTimer() {
        _currentTime.value = calculateMillis(_focusTime.value)
        _currentCycle.value = 0
        _isTimerRunning.value = false
        _mode.value = TimerMode.Focus
    }

    private suspend fun runTimer() {
        while (_isTimerRunning.value && _currentCycle.value < _numberOfCycles.value) {
            delay(100L)
            if (_currentTime.value > 0) {
                _currentTime.value -= 100L
            } else {
                if (_mode.value == TimerMode.Focus) {
                    _mode.value = TimerMode.Rest
                    _currentTime.value = calculateMillis(_restTime.value)
                } else {
                    _mode.value = TimerMode.Focus
                    _currentTime.value = calculateMillis(_focusTime.value)
                    _currentCycle.value += 1
                }
            }
        }
        _isTimerRunning.value = false
    }
}
