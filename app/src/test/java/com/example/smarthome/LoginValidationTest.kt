package com.example.smarthome

import org.junit.Assert
import org.junit.Test

class LoginValidationTest {

    private fun isLoginInputValid(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    @Test
    fun login_with_empty_email_fails() {
        val result = isLoginInputValid("", "password123")
        Assert.assertFalse(result)
    }

    @Test
    fun login_with_valid_inputs_passes() {
        val result = isLoginInputValid("test@email.com", "password123")
        Assert.assertTrue(result)
    }
}
