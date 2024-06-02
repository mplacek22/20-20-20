package com.pwr.app20_20_20.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.pwr.app20_20_20.R
import com.pwr.app20_20_20.storage.AppDatabase
import com.pwr.app20_20_20.storage.EyeExercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate

//data class EyeExercise(
//    val id: String,
//    val name: String,
//    val instruction: String,
//    val mediaUri: Uri
//)

class EyeExerciseViewModel(context: Context) : ViewModel() {
    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "eye_exercises_database"
    ).build()

    private val eyeExerciseDao = database.eyeExerciseDao()

    val exercises: Flow<List<EyeExercise>> = eyeExerciseDao.getAllExercises()

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

    fun getExercise(exerciseId: String): Flow<EyeExercise> {
        return eyeExerciseDao.getExerciseById(exerciseId)
    }

    suspend fun getAllExercises(): List<EyeExercise> {
        return eyeExerciseDao.getAllExercisesList()
    }

    fun insertExercise(exercise: EyeExercise) {
        viewModelScope.launch {
            eyeExerciseDao.insertExercise(exercise)
        }
    }

    fun insertAllExercises(exercises: List<EyeExercise>) {
        viewModelScope.launch {
            eyeExerciseDao.insertAll(exercises)
        }
    }
}
