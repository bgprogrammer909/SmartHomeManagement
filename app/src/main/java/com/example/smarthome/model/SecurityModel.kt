package com.example.smarthome.model

data class SecurityModel(
    val activeMode: Boolean = true,
    val motionDetection: Boolean = true,
    val doorSensors: Boolean = true,
    val pushNotifications: Boolean = true,
    val recentActivities: List<ActivityLog> = listOf(
        ActivityLog("Motion detected – Front door", "2 min ago"),
        ActivityLog("Door opened – Main entrance", "15 min ago"),
        ActivityLog("Camera triggered – Backyard", "1 hour ago")
    )
)

data class ActivityLog(
    val description: String = "",
    val timestamp: String = ""
)
