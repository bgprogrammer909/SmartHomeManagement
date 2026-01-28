package com.example.smarthome.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.smarthome.model.ClimateModel
import com.example.smarthome.repo.ClimateRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class ClimateViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockRepo: ClimateRepo

    private lateinit var viewModel: ClimateViewModel
    private val testUserId = "test_user_123"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch realtime data and update state`() = runTest {
        // Given
        val expectedModel = ClimateModel(
            fanSpeed = 2,
            powerOn = true,
            autoMode = false,
            temperature = 25
        )

        // Mock the repository to call the callback with success immediately
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, ClimateModel?) -> Unit>(1)
            callback(true, expectedModel)
            null
        }.whenever(mockRepo).getFanRealtime(eq(testUserId), any())

        // When
        viewModel = ClimateViewModel(mockRepo, testUserId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockRepo).getFanRealtime(eq(testUserId), any())
        assert(viewModel.state.value == expectedModel)
    }

    @Test
    fun `setFanSpeed should update state and persist to repository`() = runTest {
        // Given
        val initialModel = ClimateModel(fanSpeed = 0, powerOn = true, autoMode = false, temperature = 28)
        setupViewModelWithInitialState(initialModel)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(true, null)
            null
        }.whenever(mockRepo).updateFan(eq(testUserId), any(), any())

        // When
        viewModel.setFanSpeed(2)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.state.value.fanSpeed == 2)
        verify(mockRepo).updateFan(
            eq(testUserId),
            argThat { fanSpeed == 2 },
            any()
        )
    }

    @Test
    fun `setPower should update state and persist to repository`() = runTest {
        // Given
        val initialModel = ClimateModel(fanSpeed = 1, powerOn = true, autoMode = false, temperature = 28)
        setupViewModelWithInitialState(initialModel)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(true, null)
            null
        }.whenever(mockRepo).updateFan(eq(testUserId), any(), any())

        // When
        viewModel.setPower(false)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.state.value.powerOn == false)
        verify(mockRepo).updateFan(
            eq(testUserId),
            argThat { powerOn == false },
            any()
        )
    }

    @Test
    fun `setAutoMode should update state and persist to repository`() = runTest {
        // Given
        val initialModel = ClimateModel(fanSpeed = 1, powerOn = true, autoMode = false, temperature = 28)
        setupViewModelWithInitialState(initialModel)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(true, null)
            null
        }.whenever(mockRepo).updateFan(eq(testUserId), any(), any())

        // When
        viewModel.setAutoMode(true)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.state.value.autoMode == true)
        verify(mockRepo).updateFan(
            eq(testUserId),
            argThat { autoMode == true },
            any()
        )
    }

    @Test
    fun `setTemperature should update state and persist to repository`() = runTest {
        // Given
        val initialModel = ClimateModel(fanSpeed = 1, powerOn = true, autoMode = false, temperature = 28)
        setupViewModelWithInitialState(initialModel)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(true, null)
            null
        }.whenever(mockRepo).updateFan(eq(testUserId), any(), any())

        // When
        viewModel.setTemperature(22)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.state.value.temperature == 22)
        verify(mockRepo).updateFan(
            eq(testUserId),
            argThat { temperature == 22 },
            any()
        )
    }

    @Test
    fun `update should persist to repository even if it fails`() = runTest {
        // Given
        val initialModel = ClimateModel(fanSpeed = 0, powerOn = true, autoMode = false, temperature = 28)
        setupViewModelWithInitialState(initialModel)

        // Mock repository failure
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(false, "Network error")
            null
        }.whenever(mockRepo).updateFan(eq(testUserId), any(), any())

        // When
        viewModel.setFanSpeed(3)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        // State should still be updated locally even if Firebase update fails
        assert(viewModel.state.value.fanSpeed == 3)
        verify(mockRepo).updateFan(eq(testUserId), any(), any())
    }

    @Test
    fun `init should handle repository failure gracefully`() = runTest {
        // Given
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, ClimateModel?) -> Unit>(1)
            callback(false, null)
            null
        }.whenever(mockRepo).getFanRealtime(eq(testUserId), any())

        // When
        viewModel = ClimateViewModel(mockRepo, testUserId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockRepo).getFanRealtime(eq(testUserId), any())
        // State should remain at default values
        assert(viewModel.state.value == ClimateModel())
    }

    @Test
    fun `multiple updates should preserve other fields`() = runTest {
        // Given
        val initialModel = ClimateModel(fanSpeed = 1, powerOn = true, autoMode = true, temperature = 25)
        setupViewModelWithInitialState(initialModel)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(true, null)
            null
        }.whenever(mockRepo).updateFan(eq(testUserId), any(), any())

        // When
        viewModel.setFanSpeed(2)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val currentState = viewModel.state.value
        assert(currentState.fanSpeed == 2)
        assert(currentState.powerOn == true) // Should be preserved
        assert(currentState.autoMode == true) // Should be preserved
        assert(currentState.temperature == 25) // Should be preserved
    }

    private fun setupViewModelWithInitialState(initialModel: ClimateModel) {
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, ClimateModel?) -> Unit>(1)
            callback(true, initialModel)
            null
        }.whenever(mockRepo).getFanRealtime(eq(testUserId), any())

        viewModel = ClimateViewModel(mockRepo, testUserId)
        testDispatcher.scheduler.advanceUntilIdle()
    }
}