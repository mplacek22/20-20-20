package com.pwr.app20_20_20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pwr.app20_20_20.ui.theme.AppTheme
import com.pwr.app20_20_20.viewmodels.EyeExerciseViewModel
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import com.pwr.app20_20_20.viewmodels.TimerViewModelFactory

class MainActivity : ComponentActivity() {

    private val timerViewModel: TimerViewModel by viewModels {
        TimerViewModelFactory()
    }

    private val eyeExerciseViewModel = EyeExerciseViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                darkTheme = true,
                dynamicColor = false
            ){
                Navigation(timerViewModel = timerViewModel, exerciseViewModel = eyeExerciseViewModel)
            }
        }
    }
}