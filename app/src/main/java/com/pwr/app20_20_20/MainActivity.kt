package com.pwr.app20_20_20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pwr.app20_20_20.ui.theme.AppTheme
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import com.pwr.app20_20_20.viewmodels.TimerViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: TimerViewModel by viewModels {
        TimerViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                darkTheme = true,
                dynamicColor = false
            ){
                Navigation(viewModel = viewModel)
            }
        }
    }
}