package com.pwr.app20_20_20.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.pwr.app20_20_20.R
import com.pwr.app20_20_20.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDoListScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        topBar = { TopBar() },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding))
            ) {
            Text(
                text = "Eye Care To Do List",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            val tasks = remember {
                mutableStateListOf(
                    Task("Palming", true),
                    Task("Blinking", true),
                    Task("Pencil Push-Ups", true),
                    Task("Near And Far Focus", false),
                    Task("Figure 8", false)
                )
            }

            tasks.forEach { task ->
                TaskItem(task) { updatedTask ->
                    val index = tasks.indexOf(task)
                    tasks[index] = updatedTask
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskUpdated: (Task) -> Unit) {
    var showDetails by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { showDetails = !showDetails }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onTaskUpdated(task.copy(completed = !task.completed)) }) {
                Icon(
                    imageVector = if (task.completed) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                    contentDescription = "Check task",
                    tint = Color.White
                )
            }

            Text(
                text = task.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }

        if (showDetails) {
            Text(
                text = getExerciseDetails(task.title),
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 32.dp, top = 8.dp)
            )
        }
    }
}

data class Task(val title: String, val completed: Boolean)

fun getExerciseDetails(title: String): String {
    return when (title) {
        "Palming" -> """
            Palming is a yogic eye exercise, suggesting relaxing the muscles around the eyes, reducing eye fatigue.
            To palm, start by rubbing your hands together to warm them up. Close your eyes and place the palm of each hand over the corresponding cheekbone. Cup your hand over each eye and breathe deeply for five minutes.
        """.trimIndent()
        "Blinking" -> """
            Blinking is a simple yet effective way to refresh your eyes.
            Blink every 2-3 seconds for a couple of minutes to keep your eyes lubricated and reduce eye strain.
        """.trimIndent()
        "Pencil Push-Ups" -> """
            Pencil Push-Ups can help improve convergence and focus.
            Hold a pencil at arm's length and slowly bring it towards your nose while keeping it in focus. Repeat this several times.
        """.trimIndent()
        "Near And Far Focus" -> """
            Near and Far Focus can improve the flexibility of your eye muscles.
            Focus on a nearby object for 15 seconds, then shift your gaze to a faraway object for another 15 seconds. Repeat the cycle.
        """.trimIndent()
        "Figure 8" -> """
            Figure 8 helps to improve the flexibility and coordination of your eye muscles.
            Imagine a figure 8 lying on its side. Trace this pattern with your eyes slowly for a couple of minutes.
        """.trimIndent()
        else -> "Details not available."
    }
}
