package com.example.smarthome.repo

import com.example.smarthome.model.LightModel

interface PLightRepo {
    fun getLightsRealtime(userId: String, callback: (success: Boolean, data: LightModel?) -> Unit)
    fun updateLights(userId: String, model: LightModel, callback: (success: Boolean, error: String?) -> Unit)
}