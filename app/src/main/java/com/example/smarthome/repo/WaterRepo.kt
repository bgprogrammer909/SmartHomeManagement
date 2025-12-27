package com.example.smarthome.repo

import com.example.smarthome.model.WaterModel
import kotlinx.coroutines.flow.StateFlow

interface WaterRepository {

    // Observe water state in real-time
    fun getWaterRealtime(): StateFlow<WaterModel>

    fun togglePump()
    fun toggleAutoMode()
    fun turnOn()
    fun turnOff()
}
