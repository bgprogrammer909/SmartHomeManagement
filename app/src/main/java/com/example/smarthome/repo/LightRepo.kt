package com.example.smarthome.repo

import com.example.smarthome.model.LightModel

// Interface for managing smart lights in a repository (e.g., Firebase)
interface LightRepo {

    /**
     * Fetches the current state of lights in real-time.
     * @param callback Returns success status and LightModel data (or null if failed)
     */
    fun getLightsRealtime(callback: (success: Boolean, data: LightModel?) -> Unit)

    /**
     * Updates a specific light's state and brightness.
     * @param lightNumber The light to update (1 or 2)
     * @param isOn Whether the light is on or off
     * @param brightness Brightness value (0-100)
     * @param callback Returns success status and optional error message
     */
    fun updateLight(
        lightNumber: Int,
        isOn: Boolean,
        brightness: Float,
        callback: (Boolean, String?) -> Unit
    )
}
