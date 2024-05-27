package com.pwr.app20_20_20.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pwr.app20_20_20.BottomNavigationBar
import com.pwr.app20_20_20.R
import com.pwr.app20_20_20.TopBar
import com.pwr.app20_20_20.viewmodels.TimerMode
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(navController: NavController, viewModel: TimerViewModel = viewModel()) {

    val numberOfCycles by viewModel.numberOfCycles.collectAsState()
    val focusTime by viewModel.focusTime.collectAsState()
    val restTime by viewModel.restTime.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val mode by viewModel.mode.collectAsState()
    val currentCycle by viewModel.currentCycle.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        topBar = { TopBar() },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${mode.name} Mode",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 15.dp, top = 50.dp)
            )

            Text(
                text = "${numberOfCycles - currentCycle} cycles left",
                color = Color.White,
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 100.dp)
            )

            Timer(
                focusTime = viewModel.calculateMillis(focusTime),
                restTime = viewModel.calculateMillis(restTime),
                currentTime = currentTime,
                isTimerRunning = isTimerRunning,
                handleColorFocus = Color(0xFF0FB04C),
                handleColorRest = Color(0xFFFF9800),
                activeBarColorFocus = Color(0xFF0FB04C),
                activeBarColorRest = Color(0xFFFF9800),
                modifier = Modifier.size(250.dp),
                strokeWidth = 11.dp,
                mode = mode,
                startTimer = { viewModel.startTimer() },
                stopTimer = { viewModel.stopTimer() },
                resetTimer = { viewModel.resetTimer() }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Timer(
    focusTime: Long,
    restTime: Long,
    currentTime: Long,
    isTimerRunning: Boolean,
    handleColorFocus: Color = Color(0xFF0FB04C),
    handleColorRest: Color = Color.Red,
    inactiveBarColor: Color = Color.DarkGray,
    activeBarColorFocus: Color = Color(0xFF0FB04C),
    activeBarColorRest: Color = Color(0xFFF44336),
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 5.dp,
    mode: TimerMode,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
    resetTimer: () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val value = currentTime.toFloat() / (if (mode == TimerMode.Focus) focusTime else restTime)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged {
            size = it
        }.combinedClickable(
            onClick = {
                if (currentTime <= 0L) {
                    resetTimer()
                } else {
                    if (isTimerRunning) {
                        stopTimer()
                    } else {
                        startTimer()
                    }
                }
            },
            onLongClick = {
                if (!isTimerRunning) {
                    resetTimer()
                }
            },
        )
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
                color = if (mode == TimerMode.Focus) activeBarColorFocus else activeBarColorRest,
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
                color = if (mode == TimerMode.Focus) handleColorFocus else handleColorRest,
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
    val totalSeconds = ceil(timeInMillis.toDouble() / 1000).toLong()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return if (minutes > 0) {
        String.format("%02d:%02d", minutes, seconds)
    } else {
        String.format("%02d", seconds)
    }
}