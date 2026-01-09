package com.example.smarthome.model

data class WaterModel(
    val isPumpOn: Boolean = true,
    val autoMode: Boolean = false,
    val todayUsage: Double = 0.0,
    val flowRate: Double = 0.0,
    val energySavings: Int = 15
)
