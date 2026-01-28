// SecurityModel.kt
package com.example.smarthome.model

enum class SecurityMode(val title: String, val description: String, val icon: Int) {
    HOME("Home", "Minimal security, you are home", 0),
    AWAY("Away", "Full security, you are away", 0),
    NIGHT("Night", "Sleep mode with perimeter guard", 0)
}

data class SecurityModel(
    val activeMode: String = "HOME",
    val motionDetection: Boolean = true,
    val doorSensors: Boolean = true,
    val pushNotifications: Boolean = true,
    val recentActivities: List<ActivityLog> = listOf(
        ActivityLog("Motion detected – Front door", "2 min ago"),
        ActivityLog("Door opened – Main entrance", "15 min ago"),
        ActivityLog("Camera triggered – Backyard", "1 hour ago")
    )
)
