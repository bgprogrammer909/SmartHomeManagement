package com.example.smarthome.repo

import com.example.smarthome.model.WaterModel

interface WaterRepo {
    fun getWaterRealtime(userId: String, callback: (success: Boolean, data: WaterModel?) -> Unit)
    fun updateWater(userId: String, model: WaterModel, callback: (success: Boolean, error: String?) -> Unit)
}
