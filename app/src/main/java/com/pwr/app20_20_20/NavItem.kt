package com.pwr.app20_20_20

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String)
{
    data object HomeScreen : NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, "home_screen")
    data object SettingsScreen: NavItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings,"settings_screen")
    data object HowToUseScreen: NavItem("How to use", Icons.Filled.Info, Icons.Outlined.Info,"how_to_use_screen")
    data object ToDoListScreen: NavItem("To do", Icons.Filled.List, Icons.Outlined.List, "to_do_list_screen")

    fun withArgs( vararg args: String): String{
        return buildString {
            append(route)
            args.forEach {arg ->
                append("/$arg")
            }
        }
    }
}