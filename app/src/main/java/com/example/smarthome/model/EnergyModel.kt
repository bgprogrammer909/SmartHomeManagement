package com.example.smarthome.model

data class EnergyModel(
    val energyHistory: Map<String, Float> = emptyMap(), // "dd-MM-yyyy" -> kWh
    val graphData: List<Pair<String, Float>> = emptyList(), // Filtered data for graph
    val totalKwh: Float = 0f,
    val selectedMonth: Int = 0, // 1-12
    val selectedYear: Int = 0,
    val availableMonths: List<String> = emptyList(), // "Jan 2026", "Feb 2026", etc.
    val availableYears: List<Int> = emptyList() // [2024, 2025, 2026]
)