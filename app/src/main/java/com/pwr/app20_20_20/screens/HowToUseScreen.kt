package com.pwr.app20_20_20.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.pwr.app20_20_20.BottomNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HowToUseScreen (navController: NavController){
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    )
    {
        Column {
            Text(text = "How To Use Screen")
        }
    }
}