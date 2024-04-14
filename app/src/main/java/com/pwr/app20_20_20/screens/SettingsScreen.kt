package com.pwr.app20_20_20.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pwr.app20_20_20.BottomNavigationBar
import com.pwr.app20_20_20.R
import com.pwr.app20_20_20.viewmodels.TimerViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen (navController: NavController, viewModel: TimerViewModel){
    val focusTime = viewModel.focusTime.collectAsState().value
    val restTime = viewModel.restTime.collectAsState().value
    val numberOfCycles = viewModel.numberOfCycles.collectAsState().value

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Black,
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing))
        ) {
            Text("Number of cycles", color = Color.White)
            Slider(
                value = numberOfCycles.toFloat(),
                onValueChange = { newValue ->
                    viewModel.setNumberOfCycles(newValue.toInt())
                },
                valueRange = viewModel.cycleRange.first.toFloat()..viewModel.cycleRange.last.toFloat(),
                steps = viewModel.cycleRange.last - viewModel.cycleRange.first - 1
            )
            Text("Focus time", color = Color.White)
            TimeEditor(
                minutes = focusTime.minutes,
                seconds = focusTime.seconds,
                onMinutesChange = viewModel::setFocusTimeMinutes,
                onSecondsChange = viewModel::setFocusTimeSeconds
            )
            Text("Rest time", color = Color.White)
            TimeEditor(
                minutes = restTime.minutes,
                seconds = restTime.seconds,
                onMinutesChange = viewModel::setRestTimeMinutes,
                onSecondsChange = viewModel::setRestTimeSeconds
            )
        }
    }
}

@Composable
fun TimeEditor(
    minutes: Int,
    seconds: Int,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        TimeDisplay(value = minutes, onValueChange = onMinutesChange, label = "Minutes")
        Text(
            text = ":",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        TimeDisplay(value = seconds, onValueChange = onSecondsChange, label = "Seconds")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDisplay(value: Int, onValueChange: (Int) -> Unit, label: String) {
    var textValue by remember { mutableStateOf(value.toString().padStart(2, '0')) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(Color.Black, MaterialTheme.shapes.medium)
        ) {
            TextField(
                value = textValue,
                onValueChange = { newValue ->
                    // Only accept numbers and limit to 2 characters
                    if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
                        textValue = newValue
                        onValueChange(newValue.toIntOrNull() ?: value)
                    }
                },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.background(Color.Transparent), // This sets the TextField background
                colors = TextFieldDefaults.textFieldColors( // Correct way to set TextField background color
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.White)
    }
}
