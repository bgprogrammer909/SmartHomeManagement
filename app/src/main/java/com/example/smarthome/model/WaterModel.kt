package com.example.smarthome.model

data class WaterModel(
    val isPumpOn: Boolean = false,
    val autoMode: Boolean = false,
    val todayUsage: Double = 0.0,      // in liters
    val flowRate: Double = 0.0,         // in L/min
    val energySavings: Int = 15         // percentage
)