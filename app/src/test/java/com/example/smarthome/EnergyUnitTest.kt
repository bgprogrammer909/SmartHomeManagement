package com.example.smarthome.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.smarthome.model.EnergyModel
import com.example.smarthome.repo.EnergyRepo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

class EnergyViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepo: EnergyRepo

    private lateinit var viewModel: EnergyViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        // Mock realtime energy observation
        doAnswer { invocation ->
            val callback = invocation.getArgument<(EnergyModel) -> Unit>(0)
            callback(EnergyModel()) // default initial data
            null
        }.whenever(mockRepo).observeEnergy(any())

        viewModel = EnergyViewModel(mockRepo)
    }

    @Test
    fun `init should observe energy data`() {
        verify(mockRepo).observeEnergy(any())
    }

    @Test
    fun `updateTotalUsage should update state and call repository`() {
        viewModel.updateTotalUsage(120.5)

        assert(viewModel.state.value.totalUsage == 120.5)
        verify(mockRepo).updateEnergy(argThat { totalUsage == 120.5 })
    }

    @Test
    fun `updateTodayUsage should update state and call repository`() {
        viewModel.updateTodayUsage(15.0)

        assert(viewModel.state.value.todayUsage == 15.0)
        verify(mockRepo).updateEnergy(argThat { todayUsage == 15.0 })
    }

    @Test
    fun `updateWeekUsage should update state and call repository`() {
        viewModel.updateWeekUsage(80.0)

        assert(viewModel.state.value.weekUsage == 80.0)
        verify(mockRepo).updateEnergy(argThat { weekUsage == 80.0 })
    }

    @Test
    fun `updateMonthUsage should update state and call repository`() {
        viewModel.updateMonthUsage(300.0)

        assert(viewModel.state.value.monthUsage == 300.0)
        verify(mockRepo).updateEnergy(argThat { monthUsage == 300.0 })
    }

    @Test
    fun `updateLightsUsage should update state and call repository`() {
        viewModel.updateLightsUsage(5)

        assert(viewModel.state.value.lightsUsage == 5)
        verify(mockRepo).updateEnergy(argThat { lightsUsage == 5 })
    }
}
