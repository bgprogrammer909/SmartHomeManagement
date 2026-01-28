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

    @Test
    fun `updateAcUsage should update state and call repository`() {
        viewModel.updateAcUsage(2)

        assert(viewModel.state.value.acUsage == 2)
        verify(mockRepo).updateEnergy(argThat { acUsage == 2 })
    }

    @Test
    fun `updateWaterPumpUsage should update state and call repository`() {
        viewModel.updateWaterPumpUsage(1)

        assert(viewModel.state.value.waterPumpUsage == 1)
        verify(mockRepo).updateEnergy(argThat { waterPumpUsage == 1 })
    }

    @Test
    fun `updateOthersUsage should update state and call repository`() {
        viewModel.updateOthersUsage(4)

        assert(viewModel.state.value.othersUsage == 4)
        verify(mockRepo).updateEnergy(argThat { othersUsage == 4 })
    }

    @Test
    fun `multiple updates should preserve previous values`() {
        viewModel.updateTotalUsage(100.0)
        viewModel.updateLightsUsage(3)

        val state = viewModel.state.value
        assert(state.totalUsage == 100.0)
        assert(state.lightsUsage == 3)
    }
}
