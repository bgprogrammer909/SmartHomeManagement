package com.example.smarthome.model

// Represents the state of two smart lights
data class LightModel(
    val light1On: Boolean = false,       // Light 1 on/off
    val light2On: Boolean = false,       // Light 2 on/off
    val light1Brightness: Float = 50f,   // Light 1 brightness (0-100)
    val light2Brightness: Float = 50f    // Light 2 brightness (0-100)
) {
    // Computed property to count how many lights are ON
    val lightsOnCount: Int
        get() = listOf(light1On, light2On).count { it }
}
