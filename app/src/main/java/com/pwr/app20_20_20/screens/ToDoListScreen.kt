package com.pwr.app20_20_20.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pwr.app20_20_20.BottomNavigationBar
import com.pwr.app20_20_20.NavItem
import com.pwr.app20_20_20.R
import com.pwr.app20_20_20.TopBar
import com.pwr.app20_20_20.viewmodels.EyeExercise
import com.pwr.app20_20_20.viewmodels.EyeExerciseViewModel
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import kotlinx.coroutines.flow.forEach

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDoListScreen(navController: NavController, exerciseViewModel: EyeExerciseViewModel) {
    val exercises = exerciseViewModel.exercises.collectAsState().value
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        topBar = { TopBar() },
        containerColor = Color.Black
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding))
            ) {
            items(exercises) { exercise ->
                ExerciseItem(exerciseViewModel, exercise, navController)
            }
        }
    }
}

@Composable
fun ExerciseItem(viewModel: EyeExerciseViewModel, exercise: EyeExercise, navController: NavController) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(NavItem.ExerciseDetailScreen.withArgs(exercise.id))
            },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_small), Alignment.Start)
        ) {
            Icon(
                imageVector = if (viewModel.isExerciseDoneToday(exercise.id)) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
                contentDescription = "Exercise done"
            )
            Text(
                text = exercise.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

//data class Task(val title: String, val completed: Boolean)
//
//fun getExerciseDetails(title: String): String {
//    return when (title) {
//        "Palming" -> """
//            Palming is a yogic eye exercise, suggesting relaxing the muscles around the eyes, reducing eye fatigue.
//            To palm, start by rubbing your hands together to warm them up. Close your eyes and place the palm of each hand over the corresponding cheekbone. Cup your hand over each eye and breathe deeply for five minutes.
//        """.trimIndent()
//        "Blinking" -> """
//            Blinking is a simple yet effective way to refresh your eyes.
//            Blink every 2-3 seconds for a couple of minutes to keep your eyes lubricated and reduce eye strain.
//        """.trimIndent()
//        "Pencil Push-Ups" -> """
//            Pencil Push-Ups can help improve convergence and focus.
//            Hold a pencil at arm's length and slowly bring it towards your nose while keeping it in focus. Repeat this several times.
//        """.trimIndent()
//        "Near And Far Focus" -> """
//            Near and Far Focus can improve the flexibility of your eye muscles.
//            Focus on a nearby object for 15 seconds, then shift your gaze to a faraway object for another 15 seconds. Repeat the cycle.
//        """.trimIndent()
//        "Figure 8" -> """
//            Figure 8 helps to improve the flexibility and coordination of your eye muscles.
//            Imagine a figure 8 lying on its side. Trace this pattern with your eyes slowly for a couple of minutes.
//        """.trimIndent()
//        else -> "Details not available."
//    }
//}
