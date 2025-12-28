package com.example.smarthome.model

data class EnergyModel(
    val totalUsage: Double = 120.0,          // in kWh
    val percentageDiff: Int = 12,            // percentage difference
    val todayUsage: Double = 18.0,           // in kWh
    val weekUsage: Double = 120.0,           // in kWh
    val monthUsage: Double = 485.0,          // in kWh
    val dayData: List<Float> = listOf(20f, 40f, 35f, 50f, 45f, 30f, 60f),
    val weekData: List<Float> = listOf(90f, 95f, 80f, 88f, 92f, 100f, 97f),
    val monthData: List<Float> = listOf(60f, 65f, 70f, 80f, 75f, 85f, 95f),
    val lightsUsage: Int = 28,               // percentage
    val acUsage: Int = 42,                   // percentage
    val waterPumpUsage: Int = 78,            // percentage
    val othersUsage: Int = 18,               // percentage
    val estimatedBill: Double = 24.50,       // in dollars
    val savings: Double = 3.20               // in dollars
)