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
