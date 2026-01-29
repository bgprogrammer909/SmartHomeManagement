package com.example.smarthome

import com.example.smarthome.repo.EnergyRepo
import com.example.smarthome.viewmodel.EnergyViewModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class EnergyUnitTest {

    @Test
    fun energy_load_success_updates_state() {
        // Mock repo
        val repo = mock<EnergyRepo>()

        val fakeHistory = mapOf(
            "01-01-2026" to 10f,
            "02-01-2026" to 20f
        )

        // Mock callback (index 1)
        doAnswer { invocation ->
            val callback =
                invocation.getArgument<(Boolean, Map<String, Float>?) -> Unit>(1)
            callback(true, fakeHistory)
            null
        }.`when`(repo).getEnergyHistoryRealtime(eq("user123"), any())

        // Create ViewModel
        val viewModel = EnergyViewModel(repo = repo, userId = "user123")

        // Assertions
        val state = viewModel.state.value

        assertEquals(2, state.graphData.size)
        assertEquals(30f, state.totalKwh)
        assertEquals(fakeHistory, state.energyHistory)

        // Verify repo call
        verify(repo).getEnergyHistoryRealtime(eq("user123"), any())
    }

    @Test
    fun select_month_filters_data_correctly() {
        val repo = mock<EnergyRepo>()

        val history = mapOf(
            "01-01-2026" to 10f,
            "02-01-2026" to 20f,
            "01-02-2026" to 30f
        )

        doAnswer { invocation ->
            val callback =
                invocation.getArgument<(Boolean, Map<String, Float>?) -> Unit>(1)
            callback(true, history)
            null
        }.`when`(repo).getEnergyHistoryRealtime(eq("user123"), any())

        val viewModel = EnergyViewModel(repo = repo, userId = "user123")

        // Select January
        viewModel.selectMonth(1)

        val state = viewModel.state.value

        assertEquals(2, state.graphData.size)
        assertEquals(30f, state.totalKwh)
    }

    @Test
    fun select_year_filters_data_correctly() {
        val repo = mock<EnergyRepo>()

        val history = mapOf(
            "01-01-2025" to 10f,
            "01-01-2026" to 20f
        )

        doAnswer { invocation ->
            val callback =
                invocation.getArgument<(Boolean, Map<String, Float>?) -> Unit>(1)
            callback(true, history)
            null
        }.`when`(repo).getEnergyHistoryRealtime(eq("user123"), any())

        val viewModel = EnergyViewModel(repo = repo, userId = "user123")

        viewModel.selectYear(2026)

        val state = viewModel.state.value

        assertEquals(1, state.graphData.size)
        assertEquals(20f, state.totalKwh)
    }
}
