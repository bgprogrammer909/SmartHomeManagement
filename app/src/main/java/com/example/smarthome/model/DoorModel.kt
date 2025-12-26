package com.example.smarthome.model

// Represents both doors together
data class DoorModel(
    val mainDoorLocked: Boolean = true,
    val homeDoorLocked: Boolean = true
)
