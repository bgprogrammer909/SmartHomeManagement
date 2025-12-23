package com.example.smarthome.repo

import com.example.smarthome.model.LightModel

interface PLightRepo {

    fun getLightsRealtime(
        userId: String,
        callback: (success: Boolean, data: LightModel?) -> Unit
    )

    fun updateLight(
        userId: String,
        lightNumber: Int,
        isOn: Boolean,
        brightness: Float,
        callback: (Boolean, String?) -> Unit
    )
}
