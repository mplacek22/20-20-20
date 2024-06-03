package com.pwr.app20_20_20.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.pwr.app20_20_20.storage.AppDatabase

class EyeExerciseViewModelFactory(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyeExerciseViewModel::class.java)) {
            val database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "eye_exercises_database"
            ).build()
            @Suppress("UNCHECKED_CAST")
            return EyeExerciseViewModel(database, sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
