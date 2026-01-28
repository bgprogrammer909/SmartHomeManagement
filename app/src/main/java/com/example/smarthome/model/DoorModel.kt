package com.example.smarthome.model

// Represents doors state
data class DoorModel(
    val mainDoorLocked: Boolean = false,
    val homeDoorLocked: Boolean = false
) {
    // Computed property to determine if any door is locked
    val isAnyLocked: Boolean
        get() = mainDoorLocked || homeDoorLocked
}