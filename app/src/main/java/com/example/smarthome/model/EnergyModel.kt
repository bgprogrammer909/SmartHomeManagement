package com.example.smarthome.model

data class EnergyModel(
    val totalUsage: Float = 0f,

    val lightsUsage: Int = 0,
    val acUsage: Int = 0,
    val waterPumpUsage: Int = 0,
    val othersUsage: Int = 0,

    val estimatedBill: Double = 0.0,
    val savings: Double = 0.0,

    val dayData: List<EnergyPoint> = emptyList(),
    val weekData: List<EnergyPoint> = emptyList(),
    val monthData: List<EnergyPoint> = emptyList()
)
