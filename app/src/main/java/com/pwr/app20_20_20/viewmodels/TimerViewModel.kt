package com.pwr.app20_20_20.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Time(val minutes: Int, val seconds: Int)

class TimerViewModel : ViewModel() {
    private val _numberOfCycles = MutableStateFlow(1)
    val numberOfCycles: StateFlow<Int> = _numberOfCycles.asStateFlow()

    // Using a Time data class to hold both minutes and seconds
    private val _focusTime = MutableStateFlow(Time(20, 0))
    val focusTime: StateFlow<Time> = _focusTime.asStateFlow()

    private val _restTime = MutableStateFlow(Time(0, 20))
    val restTime: StateFlow<Time> = _restTime.asStateFlow()

    val cycleRange = 1..10
    val timeRange = 0..59 // Assuming you want a range of 0 to 59 for both minutes and seconds

    fun setNumberOfCycles(cycles: Int) {
        if (cycles in cycleRange) {
            viewModelScope.launch {
                _numberOfCycles.value = cycles
            }
        }
    }

    // Setters for focus time minutes and seconds
    fun setFocusTimeMinutes(minutes: Int) {
        if (minutes in timeRange) {
            viewModelScope.launch {
                _focusTime.value = _focusTime.value.copy(minutes = minutes)
            }
        }
    }

    fun setFocusTimeSeconds(seconds: Int) {
        if (seconds in timeRange) {
            viewModelScope.launch {
                _focusTime.value = _focusTime.value.copy(seconds = seconds)
            }
        }
    }

    // Setters for rest time minutes and seconds
    fun setRestTimeMinutes(minutes: Int) {
        if (minutes in timeRange) {
            viewModelScope.launch {
                _restTime.value = _restTime.value.copy(minutes = minutes)
            }
        }
    }

    fun setRestTimeSeconds(seconds: Int) {
        if (seconds in timeRange) {
            viewModelScope.launch {
                _restTime.value = _restTime.value.copy(seconds = seconds)
            }
        }
    }

}