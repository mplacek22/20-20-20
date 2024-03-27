package com.pwr.app20_20_20

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pwr.app20_20_20.screens.HomeScreen
import com.pwr.app20_20_20.screens.HowToUseScreen
import com.pwr.app20_20_20.screens.SettingsScreen
import com.pwr.app20_20_20.screens.ToDoListScreen


@Composable
fun Navigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavItem.HomeScreen.route){
        composable(route = NavItem.HomeScreen.route){
            HomeScreen(navController = navController)
        }
        composable(route = NavItem.SettingsScreen.route){
            SettingsScreen(navController = navController)
        }
        composable(route = NavItem.HowToUseScreen.route){
            HowToUseScreen(navController = navController)
        }
        composable(route = NavItem.ToDoListScreen.route){
            ToDoListScreen(navController = navController)
        }
    }
}