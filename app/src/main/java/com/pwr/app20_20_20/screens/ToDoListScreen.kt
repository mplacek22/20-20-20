package com.pwr.app20_20_20.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pwr.app20_20_20.BottomNavigationBar
import com.pwr.app20_20_20.NavItem
import com.pwr.app20_20_20.R
import com.pwr.app20_20_20.TopBar
import com.pwr.app20_20_20.storage.EyeExercise
import com.pwr.app20_20_20.viewmodels.EyeExerciseViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDoListScreen(navController: NavController, exerciseViewModel: EyeExerciseViewModel) {
    val exercises by exerciseViewModel.exercises.collectAsState(initial = emptyList())
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
            .padding(vertical = dimensionResource(id = R.dimen.padding))
            .clickable {
                navController.navigate(NavItem.ExerciseDetailScreen.withArgs(exercise.id))
            },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.spacing_medium)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_small), Alignment.Start)
        ) {
            Icon(
                imageVector = if (viewModel.isExerciseDoneToday(exercise.id)) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
                contentDescription = stringResource(R.string.exercise_done)
            )
            Text(
                text = exercise.name,
                color = Color.White,
                fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}