package com.pwr.app20_20_20

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pwr.app20_20_20.util.MediaPlayerManager
import com.pwr.app20_20_20.viewmodels.Time
import com.pwr.app20_20_20.viewmodels.TimerMode
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class TimerViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TimerViewModel
    private lateinit var mediaPlayerManager: MediaPlayerManager
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TimerViewModel()
        mediaPlayerManager = Mockito.mock(MediaPlayerManager::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialValues() = runTest(testDispatcher) {
        assertEquals(1, viewModel.numberOfCycles.first())
        assertEquals(Time(20, 0), viewModel.focusTime.first())
        assertEquals(Time(0, 20), viewModel.restTime.first())
        assertEquals(1200000L, viewModel.currentTime.first())
        assertFalse(viewModel.isTimerRunning.first())
        assertEquals(TimerMode.Focus, viewModel.mode.first())
        assertEquals(0, viewModel.currentCycle.first())
    }

    @Test
    fun testStartTimer() = runTest(testDispatcher) {
        viewModel.startTimer(mediaPlayerManager)
        assertTrue(viewModel.isTimerRunning.first())
    }

    @Test
    fun testStopTimer() = runTest(testDispatcher) {
        viewModel.startTimer(mediaPlayerManager)
        viewModel.stopTimer()
        assertFalse(viewModel.isTimerRunning.first())
    }
}


