package com.pwr.app20_20_20.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.pwr.app20_20_20.BottomNavigationBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: TimerViewModel){

    val numberOfCycles by viewModel.numberOfCycles.collectAsState()
    val focusTime by viewModel.focusTime.collectAsState()
    val restTime by viewModel.restTime.collectAsState()

    val focusMillis = (focusTime.minutes * 60 + focusTime.seconds) * 1000L
    val restMillis = (restTime.minutes * 60 + restTime.seconds) * 1000L

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Black
    )
    {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            val (mode, setMode) = remember { mutableStateOf("Focus") }

            Text(
                text = "$mode Mode",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Timer(
                focusTime = focusMillis,
                restTime = restMillis,
                cycles = numberOfCycles,
//                focusTime = 3L * 1000L,
//                restTime = 2L * 1000L,
//                cycles = 4,
                handleColorFocus = Color.Green,
                handleColorRest = Color.Red,
                activeBarColorFocus = Color(0xFF37B900),
                activeBarColorRest = Color(0xFFFF9800),
                modifier = Modifier.size(250.dp),
                strokeWidth = 11.dp,
                mode = mode,
                setMode = setMode
            )
        }
    }
}

@Composable
fun Timer(
    focusTime: Long,
    restTime: Long,
    cycles: Int,
    handleColorFocus: Color = Color.Green,
    handleColorRest: Color = Color.Red,
    inactiveBarColor: Color = Color.DarkGray,
    activeBarColorFocus: Color = Color(0xFF37B900),
    activeBarColorRest: Color = Color(0xFFF44336),
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 5.dp,
    mode: String,
    setMode: (String) -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableStateOf(1f) }
    var currentTime by remember { mutableStateOf(focusTime) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var currentCycle by remember { mutableStateOf(0) }
    var isFocusTime by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime.toFloat() / (if (isFocusTime) focusTime else restTime)
        } else if (isTimerRunning && currentCycle < cycles) {
            if (isFocusTime) {
                isFocusTime = false
                currentTime = restTime
                setMode("Rest")
            } else {
                isFocusTime = true
                currentTime = focusTime
                currentCycle++
                setMode("Focus")
            }
        } else if (currentCycle >= cycles) {
            isTimerRunning = false
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged {
            size = it
        }.clickable {
            if (!isTimerRunning || currentTime <= 0L) {
                isTimerRunning = true
                currentTime = if (isFocusTime) focusTime else restTime
            } else {
                isTimerRunning = !isTimerRunning
            }
        }
    ) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = if (isFocusTime) activeBarColorFocus else activeBarColorRest,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = if (isFocusTime) handleColorFocus else handleColorRest,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }
        Text(
            text = formatTime(currentTime),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = if (isTimerRunning) "Press to stop" else "Press to start",
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
        )
    }
}

fun formatTime(timeInMillis: Long): String {
    val totalSeconds = timeInMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return if (minutes > 0) {
        String.format("%02d:%02d", minutes, seconds)
    } else {
        String.format("%02d", seconds)
    }
}
