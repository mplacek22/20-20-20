package com.pwr.app20_20_20

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

val bottomNavItems = listOf(
    NavItem.HomeScreen,
    NavItem.SettingsScreen,
    NavItem.HowToUseScreen,
    NavItem.ToDoListScreen
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val currentRoute = currentRoute(navController)
        bottomNavItems.forEach { item ->
            val selected = item.route == currentRoute
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid multiple copies of the same destination when reselecting the same item
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination on the stack
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}