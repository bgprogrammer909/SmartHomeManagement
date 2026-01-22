package com.example.smarthome

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.smarthome.view.EnergyAnalyticsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EnergyAnalyticsComposeTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<EnergyAnalyticsActivity>()

    @Test
    fun energy_screen_opens_successfully() {
        composeRule.onNodeWithText("Energy Analytics")
            .assertIsDisplayed()
    }

    @Test
    fun total_usage_card_is_displayed() {
        composeRule.onNodeWithTag("totalUsageCard")
            .assertIsDisplayed()
    }

    @Test
    fun day_week_month_tabs_are_clickable() {
        composeRule.onNodeWithTag("DayTab").performClick()
        composeRule.onNodeWithTag("WeekTab").performClick()
        composeRule.onNodeWithTag("MonthTab").performClick()
    }
}