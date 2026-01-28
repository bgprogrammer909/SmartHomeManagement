package com.example.smarthome

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.smarthome.view.ClimateControlActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClimateControlComposeTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    private fun launchActivity() {
        val intent = Intent(
            androidx.test.platform.app.InstrumentationRegistry
                .getInstrumentation().targetContext,
            ClimateControlActivity::class.java
        ).apply {
            putExtra("USER_ID", "test_user_123")
        }
        ActivityScenario.launch<ClimateControlActivity>(intent)
    }

    //  SCREEN LOAD TEST
    @Test
    fun climate_screen_opens_successfully() {
        launchActivity()

        composeRule.onNodeWithText("Climate Control")
            .assertIsDisplayed()
    }

    //  BACK BUTTON TEST
    @Test
    fun back_button_is_clickable() {
        launchActivity()

        composeRule.onNodeWithText("Back")
            .performClick()
    }

    //  FAN SPEED CHIPS TEST
    @Test
    fun fan_speed_chips_are_clickable() {
        launchActivity()

        composeRule.onNodeWithText("Low").performClick()
        composeRule.onNodeWithText("Medium").performClick()
        composeRule.onNodeWithText("High").performClick()
        composeRule.onNodeWithText("Turbo").performClick()
    }

    //  POWER SWITCH DISPLAY TEST
    @Test
    fun power_card_is_displayed() {
        launchActivity()

        composeRule.onNodeWithText("Power")
            .assertIsDisplayed()
    }

    //  AUTO MODE SWITCH DISPLAY TEST
    @Test
    fun auto_mode_card_is_displayed() {
        launchActivity()

        composeRule.onNodeWithText("Auto Mode")
            .assertIsDisplayed()
    }

    //  ENERGY EFFICIENCY CARD TEST
    @Test
    fun energy_efficiency_card_is_displayed() {
        launchActivity()

        composeRule.onNodeWithText("Energy Efficiency")
            .assertIsDisplayed()
    }
}

