package com.example.smarthome.model

data class AdminModel(
    val id: String = "",
    val email: String = "",
    val isActive: Boolean = true,
    val lights: Boolean = false,
    val fan: Boolean = false,
    val door: Boolean = false
)
