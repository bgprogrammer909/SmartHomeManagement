package com.example.smarthome

import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.smarthome.view.WaterActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaterComposeTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    private fun launchWaterScreen() {
        val intent = Intent(
            androidx.test.platform.app.InstrumentationRegistry
                .getInstrumentation().targetContext,
            WaterActivity::class.java
        ).apply {
            putExtra("USER_ID", "test_user_123")
        }
        ActivityScenario.launch<WaterActivity>(intent)
    }

    //  SCREEN LOAD TEST
    @Test
    fun water_screen_opens_successfully() {
        launchWaterScreen()

        composeRule.onNodeWithText("Water Pump")
            .assertIsDisplayed()
    }

    //  BACK BUTTON
    @Test
    fun back_button_is_clickable() {
        launchWaterScreen()

        composeRule.onNodeWithTag("backButton")
            .performClick()
    }


    //  PUMP TOGGLE BUTTON
    @Test
    fun pump_toggle_button_works() {
        launchWaterScreen()

        composeRule.onNodeWithTag("pumpToggleButton")
            .performClick()
    }

    //  PUMP STATUS TEXT
    @Test
    fun pump_status_is_displayed() {
        launchWaterScreen()

        composeRule.onNodeWithTag("pumpStatusText")
            .assertIsDisplayed()
    }

    // AUTO MODE SWITCH
    @Test
    fun auto_mode_switch_is_clickable() {
        launchWaterScreen()

        composeRule.onNodeWithTag("autoModeSwitch")
            .performClick()
    }

    // USAGE INFO
    @Test
    fun usage_info_is_displayed() {
        launchWaterScreen()

        composeRule.onNodeWithTag("Today's Usage_info")
            .assertIsDisplayed()
    }

    //  FLOW RATE INFO
    @Test
    fun flow_rate_info_is_displayed() {
        launchWaterScreen()

        composeRule.onNodeWithTag("Flow Rate_info")
            .assertIsDisplayed()
    }

    //  ENERGY EFFICIENCY CARD
    @Test
    fun energy_efficiency_card_is_displayed() {
        launchWaterScreen()

        composeRule.onNodeWithTag("energyEfficiencyCard")
            .assertIsDisplayed()
    }

}
