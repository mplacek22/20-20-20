package com.pwr.app20_20_20.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.app20_20_20.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate

data class EyeExercise(
    val id: String,
    val name: String,
    val instruction: String,
    val mediaUri: Uri // This could be a URL to a video or image
)

class EyeExerciseViewModel : ViewModel() {
    private val _exercises = MutableStateFlow(
        listOf(
            EyeExercise(
                "2",
                "Blinking",
                "Blinking is a simple yet effective way to refresh your eyes. Blink every 2-3 seconds for a couple of minutes to keep your eyes lubricated and reduce eye strain.",
                Uri.parse("android.resource://com.pwr.app20_20_20/" + R.raw.blinking),
            ),
            // Add more exercises here
        )
    )
    val exercises = _exercises.asStateFlow()

    private val _history = MutableStateFlow<Map<String, List<LocalDate>>>(emptyMap())
    val history = _history.asStateFlow()

    fun recordExerciseDone(exerciseId: String) {
        viewModelScope.launch {
            val today = LocalDate.now(Clock.systemUTC())
            val updatedHistory = _history.value.toMutableMap()
            val currentHistory = updatedHistory[exerciseId]?.toMutableList() ?: mutableListOf()

            if (!currentHistory.contains(today)) {
                currentHistory.add(today)
                updatedHistory[exerciseId] = currentHistory
            }

            _history.value = updatedHistory
        }
    }

    fun isExerciseDoneToday(exerciseId: String): Boolean {
        val today = LocalDate.now(Clock.systemUTC())
        return _history.value[exerciseId]?.contains(today) ?: false
    }

    fun getExercise(exerciseId: String): EyeExercise {
        return exercises.value.find { it.id == exerciseId }
            ?: throw IllegalArgumentException("Exercise not found")
    }
}
