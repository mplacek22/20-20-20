package com.pwr.app20_20_20

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pwr.app20_20_20.storage.EyeExercise
import com.pwr.app20_20_20.storage.MediaType
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

        addInitialExercises()
    }

    private fun addInitialExercises() {
        lifecycleScope.launch {
            val existingExercises = eyeExerciseViewModel.getAllExercises().map { it.id }.toSet()
            val initialExercises = listOf(
                EyeExercise(
                    id = "1",
                    name = "Blinking",
                    instruction = "Blink every 2-3 seconds for a couple of minutes to keep your eyes lubricated and reduce eye strain.",
                    mediaUri = Uri.parse("android.resource://com.pwr.app20_20_20/" + R.raw.blinking),
                    mediaType = MediaType.VIDEO
                ),
                EyeExercise(
                    id = "2",
                    name = "Palming",
                    instruction = "Rub your hands together to warm them up. Close your eyes and place the palm of each hand over the corresponding cheekbone. Cup your hand over each eye and breathe deeply for five minutes.",
                    mediaUri = Uri.parse("android.resource://com.pwr.app20_20_20/" + R.raw.palming),
                    mediaType = MediaType.IMAGE
                ),
                EyeExercise(
                    id = "3",
                    name = "Pencil Push-Ups",
                    instruction = "Hold a pencil at arm's length and slowly bring it towards your nose while keeping it in focus. Repeat this several times.",
                    mediaUri = Uri.parse("android.resource://com.pwr.app20_20_20/" + R.raw.pushups),
                    mediaType = MediaType.IMAGE
                ),
                EyeExercise(
                    id = "4",
                    name = "Near And Far Focus",
                    instruction = "Focus on a nearby object for 15 seconds, then shift your gaze to a faraway object for another 15 seconds. Repeat the cycle.",
                    mediaUri = Uri.parse("android.resource://com.pwr.app20_20_20/" + R.raw.nearfar),
                    mediaType = MediaType.IMAGE
                ),
                EyeExercise(
                    id = "5",
                    name = "Figure 8",
                    instruction = "Imagine a figure 8 lying on its side. Trace this pattern with your eyes slowly for a couple of minutes.",
                    mediaUri = Uri.parse("android.resource://com.pwr.app20_20_20/" + R.raw.figure),
                    mediaType = MediaType.VIDEO
                )
            )

            val missingExercises = initialExercises.filter { it.id !in existingExercises }

            if (missingExercises.isNotEmpty()) {
                eyeExerciseViewModel.insertAllExercises(missingExercises)
            }
        }
    }
}