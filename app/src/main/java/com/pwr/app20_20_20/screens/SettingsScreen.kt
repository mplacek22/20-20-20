package com.pwr.app20_20_20.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
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
import com.pwr.app20_20_20.TopBar
import com.pwr.app20_20_20.viewmodels.TimerViewModel

@Composable
fun SettingsScreen(navController: NavController, viewModel: TimerViewModel) {
    val focusTime = viewModel.focusTime.collectAsState().value
    val restTime = viewModel.restTime.collectAsState().value
    val numberOfCycles = viewModel.numberOfCycles.collectAsState().value

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        topBar = { TopBar() },
        containerColor = Color.Black,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text(
                    text = "Number of cycles: $numberOfCycles",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_small)))
                Slider(
                    value = numberOfCycles.toFloat(),
                    onValueChange = { newValue ->
                        viewModel.setNumberOfCycles(newValue.toInt())
                    },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    valueRange = viewModel.cycleRange.first.toFloat()..viewModel.cycleRange.last.toFloat(),
                    steps = viewModel.cycleRange.last - viewModel.cycleRange.first - 1
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))
                Text(
                    text = "Focus time",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_small)))
                TimeEditor(
                    minutes = focusTime.minutes,
                    seconds = focusTime.seconds,
                    onMinutesChange = viewModel::setFocusTimeMinutes,
                    onSecondsChange = viewModel::setFocusTimeSeconds,
                    range = viewModel.timeRange
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))
                Text(
                    text = "Rest time",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_small)))
                TimeEditor(
                    minutes = restTime.minutes,
                    seconds = restTime.seconds,
                    onMinutesChange = viewModel::setRestTimeMinutes,
                    onSecondsChange = viewModel::setRestTimeSeconds,
                    range = viewModel.timeRange
                )
            }
        }
    }
}


@Composable
fun TimeEditor(
    minutes: Int,
    seconds: Int,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit,
    range: IntRange
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TimeInputField(value = minutes, onValueChange = onMinutesChange, label = "Minutes", range = range)
            Text(
                text = ":",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            TimeInputField(value = seconds, onValueChange = onSecondsChange, label = "Seconds", range = range)
        }
    }
}

@Composable
fun TimeInputField(value: Int, onValueChange: (Int) -> Unit, label: String, range: IntRange) {
    var textValue by remember { mutableStateOf(value.toString().padStart(2, '0')) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(Color.Transparent, RoundedCornerShape(8.dp))
        ) {
            OutlinedTextField(
                value = textValue,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.toIntOrNull() in range) {
                        textValue = newValue
                        onValueChange(newValue.toIntOrNull() ?: value)
                    }
                },
                label = {
                    Text(
                        text = label,
                        color = Color.Gray
                    )
                },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
    }
}
