package com.pwr.app20_20_20

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    NavigationBar(
        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        val currentRoute = currentRoute(navController)
        bottomNavItems.forEach { item ->
            val selected = item.route == currentRoute
            NavigationBarItem(
                icon = {
                    (if (selected) item.selectedIcon else item.unselectedIcon)?.let {
                        Icon(
                            it,
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                selected = selected,
                onClick = {
                    if (item.route == NavItem.ToDoListScreen.route) {
                        navController.navigate(item.route) {
                            popUpTo(NavItem.ToDoListScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
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
