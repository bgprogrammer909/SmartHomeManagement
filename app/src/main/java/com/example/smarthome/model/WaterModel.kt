package com.example.smarthome.model

// Represents water system state
data class WaterModel(
    val isPumpOn: Boolean = false,
    val autoMode: Boolean = false,
    val todayUsage: Double = 0.0,
    val flowRate: Double = 0.0,
    val energySavings: Int = 15
)
