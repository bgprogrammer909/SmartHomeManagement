package com.example.smarthome.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.smarthome.model.EnergyModel
import com.example.smarthome.repo.EnergyRepo
import com.example.smarthome.repo.EnergyRepoImpl
import java.text.SimpleDateFormat
import java.util.*

class EnergyViewModel(
    val repo: EnergyRepo = EnergyRepoImpl(),
    val userId: String = ""
) : ViewModel() {

    val state = mutableStateOf(EnergyModel())
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private var rawHistory: Map<String, Float> = emptyMap()

    init {
        if (userId.isNotEmpty()) {
            loadData()
        } else {
            loadMockData()
        }
    }

    private fun loadData() {
        repo.getEnergyHistoryRealtime(userId) { success, history ->
            if (success && history != null) {
                rawHistory = history
                processData()
            }
        }
    }

    private fun processData() {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        // Get available years and months
        val years = mutableSetOf<Int>()
        val months = mutableSetOf<String>()

        rawHistory.keys.forEach { dateStr ->
            try {
                val date = dateFormat.parse(dateStr)
                if (date != null) {
                    calendar.time = date
                    years.add(calendar.get(Calendar.YEAR))

                    val monthName = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(date)
                    months.add(monthName)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val selectedMonth = state.value.selectedMonth.takeIf { it > 0 } ?: currentMonth
        val selectedYear = state.value.selectedYear.takeIf { it > 0 } ?: currentYear

        // Filter data for selected month and year
        val filteredData = filterDataByMonthYear(selectedMonth, selectedYear)

        state.value = EnergyModel(
            energyHistory = rawHistory,
            graphData = filteredData,
            totalKwh = filteredData.sumOf { it.second.toDouble() }.toFloat(),
            selectedMonth = selectedMonth,
            selectedYear = selectedYear,
            availableMonths = months.sorted(),
            availableYears = years.sorted()
        )
    }

    private fun filterDataByMonthYear(month: Int, year: Int): List<Pair<String, Float>> {
        val calendar = Calendar.getInstance()
        val filtered = mutableListOf<Pair<String, Float>>()

        rawHistory.forEach { (dateStr, kwh) ->
            try {
                val date = dateFormat.parse(dateStr)
                if (date != null) {
                    calendar.time = date
                    if (calendar.get(Calendar.MONTH) + 1 == month &&
                        calendar.get(Calendar.YEAR) == year) {
                        filtered.add(dateStr to kwh)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Sort by date
        return filtered.sortedBy {
            try {
                dateFormat.parse(it.first)?.time ?: 0L
            } catch (e: Exception) {
                0L
            }
        }
    }

    fun selectMonth(month: Int) {
        state.value = state.value.copy(selectedMonth = month)
        processData()
    }

    fun selectYear(year: Int) {
        state.value = state.value.copy(selectedYear = year)
        processData()
    }

    private fun loadMockData() {
        val mockHistory = mapOf(
            "01-01-2026" to 12.5f,
            "02-01-2026" to 15.3f,
            "03-01-2026" to 18.7f,
            "04-01-2026" to 14.2f,
            "05-01-2026" to 16.8f,
            "06-01-2026" to 19.4f,
            "07-01-2026" to 17.1f,
            "08-01-2026" to 20.5f,
            "09-01-2026" to 15.9f,
            "10-01-2026" to 18.3f
        )

        rawHistory = mockHistory
        processData()
    }

    override fun onCleared() {
        super.onCleared()
        if (repo is EnergyRepoImpl) {
            repo.removeListener(userId)
        }
    }
}