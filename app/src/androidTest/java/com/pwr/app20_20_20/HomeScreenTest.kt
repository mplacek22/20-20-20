package com.pwr.app20_20_20

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.pwr.app20_20_20.screens.HomeScreen
import com.pwr.app20_20_20.viewmodels.TimerViewModel
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreenDisplaysCorrectly() {
        composeTestRule.setContent {
            val viewModel = TimerViewModel()
            val navController = rememberNavController()

            HomeScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Focus Mode").assertExists()
        composeTestRule.onNodeWithText("1 cycles left").assertExists()
        composeTestRule.onNodeWithText("20:00").assertExists()
        composeTestRule.onNodeWithText("Press to start").assertExists()
    }

    @Test
    fun testStartButtonFunctionality() {
        composeTestRule.setContent {
            val viewModel = TimerViewModel()
            val navController = rememberNavController()

            HomeScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Press to start").performClick()
        composeTestRule.onNodeWithText("Press to stop").assertExists()
        composeTestRule.onNodeWithText("Press to stop").performClick()
        composeTestRule.onNodeWithText("Press to start").assertExists()
    }
}
