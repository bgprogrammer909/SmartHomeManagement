package com.example.smarthome

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.smarthome.view.DoorLockActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertCountEquals



@RunWith(AndroidJUnit4::class)
class DoorLockComposeTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    private fun launchActivity() {
        val intent = Intent(
            androidx.test.platform.app.InstrumentationRegistry
                .getInstrumentation().targetContext,
            DoorLockActivity::class.java
        ).apply {
            putExtra("USER_ID", "test_user_123")
        }
        ActivityScenario.launch<DoorLockActivity>(intent)
    }

    //  UI LOAD TEST
    @Test
    fun door_screen_opens_successfully() {
        launchActivity()

        composeRule.onNodeWithText("Door Lock")
            .assertIsDisplayed()
    }

    // MAIN DOOR TOGGLE
    @Test
    fun main_door_toggle_button_works() {
        launchActivity()

        composeRule.onNodeWithTag("Main Door-toggle")
            .performClick()
    }

    //  HOME DOOR TOGGLE
    @Test
    fun home_door_toggle_button_works() {
        launchActivity()

        composeRule.onNodeWithTag("Home Door-toggle")
            .performClick()
    }
    //  LOCK ALL BUTTON
    @Test
    fun lock_all_button_locks_all_doors() {
        launchActivity()

        composeRule.onNodeWithText("Lock All")
            .performClick()

        composeRule.waitForIdle()

        composeRule
            .onAllNodesWithText("Locked")
            .assertCountEquals(2)
    }

}
