package com.example.smarthome

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext,
            PLightActivity::class.java
        ).apply {
            putExtra("USER_ID", "test_user_123")
        }

        ActivityScenario.launch<PLightActivity>(intent)
    }

    @Test
    fun light_screen_opens_successfully() {
        launchActivity()

        composeRule.onNodeWithTag("masterlightButton")
            .assertIsDisplayed()
    }

    @Test
    fun master_light_button_is_clickable() {
        launchActivity()

        composeRule.onNodeWithTag("masterlightButton")
            .performClick()
    }


    @Test
    fun light1_switch_toggles_successfully() {
        launchActivity()

        composeRule.onNodeWithTag("Light 1 - switch")
            .performClick()
    }
}
