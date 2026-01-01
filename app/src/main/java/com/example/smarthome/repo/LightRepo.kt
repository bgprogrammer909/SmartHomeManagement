package com.example.smarthome.repo

import com.example.smarthome.model.LightModel

// Interface for managing smart lights in a repository (e.g., Firebase)
interface LightRepo {

    fun getLightsRealtime(callback: (success: Boolean, data: LightModel?) -> Unit)


    fun updateLight(
        lightNumber: Int,
        isOn: Boolean,
        brightness: Float,
        callback: (Boolean, String?) -> Unit
    )
}
