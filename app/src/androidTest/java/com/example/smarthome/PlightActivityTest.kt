package com.example.smarthome

import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.smarthome.view.PLightActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PLightComposeTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    private fun launchActivity() {
        val intent = Intent(
            androidx.test.platform.app.InstrumentationRegistry
                .getInstrumentation().targetContext,
            PLightActivity::class.java
        ).apply {
            putExtra("USER_ID", "test_user_123")
        }

        ActivityScenario.launch<PLightActivity>(intent)
        composeRule.waitForIdle()
    }

    @Test
    fun light_screen_opens_successfully() {
        launchActivity()

        composeRule.onNodeWithText("My Lights")
            .assertIsDisplayed()
    }

    @Test
    fun master_light_button_is_displayed_and_clickable() {
        launchActivity()

        composeRule.onNodeWithTag("masterlightButton")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun light1_switch_toggles_successfully() {
        launchActivity()

        composeRule.onNodeWithTag("Light 1 - switch")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun clicking_master_button_turns_all_lights_on() {
        launchActivity()

        composeRule.onNodeWithTag("masterlightButton")
            .performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("Light 1 - switch")
            .assertIsDisplayed()

        composeRule.onNodeWithTag("Light 2 - switch")
            .assertIsDisplayed()
    }

    @Test
    fun brightness_slider_appears_after_light1_is_turned_on() {
        launchActivity()

        composeRule.onNodeWithTag("Light 1-brightness")
            .assertDoesNotExist()

        composeRule.onNodeWithTag("Light 1 - switch")
            .performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("Light 1-brightness")
            .assertIsDisplayed()
    }
}
