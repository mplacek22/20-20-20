package com.pwr.app20_20_20.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pwr.app20_20_20.screens.ExerciseScreen
import com.pwr.app20_20_20.screens.HomeScreen
import com.pwr.app20_20_20.screens.HowToUseScreen
import com.pwr.app20_20_20.screens.SettingsScreen
import com.pwr.app20_20_20.screens.ToDoListScreen
import com.pwr.app20_20_20.viewmodels.EyeExerciseViewModel
import com.pwr.app20_20_20.viewmodels.TimerViewModel


@Composable
fun Navigation (timerViewModel : TimerViewModel, exerciseViewModel: EyeExerciseViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavItem.HomeScreen.route){
        composable(route = NavItem.HomeScreen.route){
            HomeScreen(navController = navController, viewModel = timerViewModel)
        }
        composable(route = NavItem.SettingsScreen.route){
            SettingsScreen(navController = navController, viewModel = timerViewModel)
        }
        composable(route = NavItem.HowToUseScreen.route){
            HowToUseScreen(navController = navController)
        }
        composable(route = NavItem.ToDoListScreen.route){
            ToDoListScreen(navController = navController, exerciseViewModel = exerciseViewModel)
        }
        composable(
            route = NavItem.ExerciseDetailScreen.route + "/{exerciseId}",
            arguments = listOf(
                navArgument("exerciseId"){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: throw IllegalStateException("Exercise ID is required")
            ExerciseScreen(viewModel = exerciseViewModel, exerciseId = exerciseId, navController = navController)
        }
    }
}