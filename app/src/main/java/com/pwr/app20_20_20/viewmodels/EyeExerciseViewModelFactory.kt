package com.pwr.app20_20_20.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EyeExerciseViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyeExerciseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EyeExerciseViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}