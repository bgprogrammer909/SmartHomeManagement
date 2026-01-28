package com.example.smarthome

import com.example.smarthome.repo.LoginRepo
import com.example.smarthome.viewmodel.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class LoginUnitTest {

    @Test
    fun login_success_test() {
        //  Mock repo
        val repo = mock<LoginRepo>()

        // ViewModel with mocked repo
        val viewModel = LoginViewModel(repo)

        // Mock callback behavior (INDEXING)
        doAnswer { invocation ->
            val callback =
                invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(
            eq("test@gmail.com"),
            eq("123456"),
            any()
        )

        //  Capture results
        var successResult = false
        var messageResult = ""

        viewModel.login("test@gmail.com", "123456") { success, message ->
            successResult = success
            messageResult = message
        }

        // Assertions
        assertTrue(successResult)
        assertEquals("Login success", messageResult)

        //  Verify repo call
        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }

    @Test
    fun login_failure_test() {
        val repo = mock<LoginRepo>()
        val viewModel = LoginViewModel(repo)

        doAnswer { invocation ->
            val callback =
                invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(false, "Invalid credentials")
            null
        }.`when`(repo).login(
            eq("wrong@gmail.com"),
            eq("wrongpass"),
            any()
        )

        var successResult = true
        var messageResult = ""

        viewModel.login("wrong@gmail.com", "wrongpass") { success, message ->
            successResult = success
            messageResult = message
        }

        assertEquals(false, successResult)
        assertEquals("Invalid credentials", messageResult)

        verify(repo).login(eq("wrong@gmail.com"), eq("wrongpass"), any())
    }
}
