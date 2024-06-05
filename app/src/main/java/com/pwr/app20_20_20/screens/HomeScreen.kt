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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pwr.app20_20_20.composables.BottomNavigationBar
import com.pwr.app20_20_20.util.MediaPlayerManager
import com.pwr.app20_20_20.R
import com.pwr.app20_20_20.composables.TopBar
import com.pwr.app20_20_20.viewmodels.TimerMode
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(navController: NavController, viewModel: TimerViewModel = viewModel()) {
    val context = navController.context
    val mediaPlayerManager = remember { MediaPlayerManager(context) }

    val numberOfCycles by viewModel.numberOfCycles.collectAsState()
    val focusTime by viewModel.focusTime.collectAsState()
    val restTime by viewModel.restTime.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val mode by viewModel.mode.collectAsState()
    val currentCycle by viewModel.currentCycle.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayerManager.release()
        }
    }

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
                text = stringResource(R.string.mode, mode.name),
                color = Color.White,
                fontSize = dimensionResource(id = R.dimen.text_font_size).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    bottom = dimensionResource(id = R.dimen.bottom_padding),
                    top = dimensionResource(id = R.dimen.top_padding)
                )
            )

            Text(
                text = stringResource(R.string.cycles_left, numberOfCycles - currentCycle),
                color = Color.White,
                fontSize = dimensionResource(id = R.dimen.cycles_text_font_size).value.sp,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.cycles_text_bottom_padding))
            )

            Timer(
                focusTime = viewModel.calculateMillis(focusTime),
                restTime = viewModel.calculateMillis(restTime),
                currentTime = currentTime,
                isTimerRunning = isTimerRunning,
                handleColorFocus = colorResource(id = R.color.focus),
                handleColorRest = colorResource(id = R.color.rest),
                activeBarColorFocus = colorResource(id = R.color.focus),
                activeBarColorRest = colorResource(id = R.color.rest),
                modifier = Modifier.size(dimensionResource(id = R.dimen.timer_size)),
                strokeWidth = dimensionResource(id = R.dimen.stroke_width),
                mode = mode,
                startTimer = { viewModel.startTimer(mediaPlayerManager) },
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
    modifier: Modifier = Modifier,
    handleColorFocus: Color = colorResource(id = R.color.focus),
    handleColorRest: Color = Color.Red,
    inactiveBarColor: Color = Color.DarkGray,
    activeBarColorFocus: Color = colorResource(id = R.color.focus),
    activeBarColorRest: Color = colorResource(id = R.color.rest),
    strokeWidth: Dp = dimensionResource(id = R.dimen.stroke_width),
    mode: TimerMode,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
    resetTimer: () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val value = currentTime.toFloat() / (if (mode == TimerMode.Focus) focusTime else restTime)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged {
                size = it
            }
            .combinedClickable(
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
                strokeWidth = (strokeWidth * 2.5f).toPx(),
                cap = StrokeCap.Round
            )
        }
        Text(
            text = formatTime(currentTime),
            fontSize = dimensionResource(id = R.dimen.timer_text_font_size).value.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = if (isTimerRunning) stringResource(R.string.press_to_stop) else stringResource(R.string.press_to_start),
            fontSize = dimensionResource(id = R.dimen.status_text_font_size).value.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = dimensionResource(id = R.dimen.status_text_bottom_padding))
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