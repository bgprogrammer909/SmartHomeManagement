package com.example.smarthome.model

data class ClimateModel(
    val fanSpeed: Int = 0,
    val powerOn: Boolean = true,
    val autoMode: Boolean = false,
    val temperature: Int = 28
)
