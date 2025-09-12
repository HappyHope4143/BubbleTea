package com.happyhope.bubbletea

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.happyhope.bubbletea.ui.theme.BubbleTeaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.happyhope.bubbletea", appContext.packageName)
    }

    @Test
    fun welcomeScreen_displaysCorrectContent() {
        composeTestRule.setContent {
            BubbleTeaTheme {
                WelcomeScreen()
            }
        }

        // Check if the welcome message is displayed
        composeTestRule.onNodeWithText("Welcome to BubbleTea!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your favorite bubble tea ordering app").assertIsDisplayed()
    }
}
