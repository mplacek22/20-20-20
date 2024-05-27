package com.pwr.app20_20_20.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.pwr.app20_20_20.BottomNavigationBar
import com.pwr.app20_20_20.R
import com.pwr.app20_20_20.TopBar
import com.pwr.app20_20_20.storage.EyeExercise
import com.pwr.app20_20_20.viewmodels.EyeExerciseViewModel
import com.pwr.app20_20_20.viewmodels.VideoViewModel
import java.time.Clock
import java.time.LocalDate

@Composable
fun ExerciseScreen(viewModel: EyeExerciseViewModel, exerciseId: String, navController: NavController) {
    val exercise by viewModel.getExercise(exerciseId).collectAsState(initial = null)

    exercise?.let {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) },
            topBar = { TopBar(title = it.name) },
            containerColor = Color.Black
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(dimensionResource(id = R.dimen.padding))
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                item {
                    val player = remember {
                        ExoPlayer.Builder(navController.context).build().apply {
                            repeatMode = Player.REPEAT_MODE_ALL
                        }
                    }
                    VideoPlayer(viewModel = VideoViewModel(it, player))
                    DisposableEffect(Unit) {
                        onDispose {
                            player.stop()
                            player.release()
                        }
                    }
                }
                item {
                    Text(
                        text = it.instruction,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                item {
                    ExerciseButton(viewModel = viewModel, exercise = it)
                }
            }
        }
    }
}

@Composable
private fun VideoPlayer(viewModel: VideoViewModel) {
    val lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    AndroidView(
        factory = { context ->
            PlayerView(context).also {
                it.player = viewModel.player
            }
        },
        update = { playerView ->
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    playerView.onPause()
                    viewModel.player.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    playerView.onResume()
                }
                else -> Unit
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    )
}

@Composable
fun ExerciseButton(viewModel: EyeExerciseViewModel, exercise: EyeExercise) {
    val history by viewModel.history.collectAsState()
    val today = LocalDate.now(Clock.systemUTC())
    val isDone = history[exercise.id]?.contains(today) ?: false

    Button(
        onClick = { viewModel.recordExerciseDone(exercise.id) },
        modifier = Modifier.fillMaxWidth(0.75f),
        enabled = !isDone,
    ) {
        Text(if (isDone) "Done" else "Mark as done")
    }
}
