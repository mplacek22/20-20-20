package com.pwr.app20_20_20.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.pwr.app20_20_20.BottomNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDoListScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Black
    )
    {
        Column {
            Text(text = "To do list Screen", color = Color.White)
        }
    }
}