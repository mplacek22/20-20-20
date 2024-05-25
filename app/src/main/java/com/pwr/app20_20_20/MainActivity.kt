package com.pwr.app20_20_20

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pwr.app20_20_20.storage.EyeExercise
import com.pwr.app20_20_20.ui.theme.AppTheme
import com.pwr.app20_20_20.viewmodels.EyeExerciseViewModel
import com.pwr.app20_20_20.viewmodels.EyeExerciseViewModelFactory
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import com.pwr.app20_20_20.viewmodels.TimerViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val timerViewModel: TimerViewModel by viewModels {
        TimerViewModelFactory()
    }

    private val eyeExerciseViewModel: EyeExerciseViewModel by viewModels {
        EyeExerciseViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                Navigation(timerViewModel = timerViewModel, exerciseViewModel = eyeExerciseViewModel)
            }
        }

        addInitialExercisesIfNeeded()
    }

    private fun addInitialExercisesIfNeeded() {
        lifecycleScope.launch {
            val exercises = eyeExerciseViewModel.getAllExercises()
            if (exercises.isEmpty()) {
                addInitialExercises()
            }
        }
    }

    private fun addInitialExercises() {
        val initialExercises = listOf(

            EyeExercise(
                id = "1",
                name = "Blinking",
                instruction = "Blink every 2-3 seconds for a couple of minutes to keep your eyes lubricated and reduce eye strain.",
                mediaUri = Uri.parse("android.resource://com.pwr.app20_20_20/" + R.raw.blinking)
            ),
//            EyeExercise(
//                id = "2",
//                name = "Palming",
//                instruction = "Rub your hands together to warm them up. Close your eyes and place the palm of each hand over the corresponding cheekbone. Cup your hand over each eye and breathe deeply for five minutes.",
//                mediaUri = Uri.parse("android.resource://com.pwr.app20_20_20/" + R.raw.palming)
//            )
        )

        eyeExerciseViewModel.insertAllExercises(initialExercises)
    }
}