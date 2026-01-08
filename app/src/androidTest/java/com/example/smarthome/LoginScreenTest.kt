package com.example.smarthome

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.smarthome.view.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun loginScreen_isDisplayed() {
        composeTestRule
            .onNodeWithText("Sign in")
            .assertIsDisplayed()
    }
    @Test
    fun forgotPassword_dialog_opens() {
        // Click on "Forget Password?"
        composeTestRule
            .onNodeWithText("Forget Password?")
            .performClick()

        // Check dialog title appears
        composeTestRule
            .onNodeWithText("Forgot Password")
            .assertIsDisplayed()
    }
}
