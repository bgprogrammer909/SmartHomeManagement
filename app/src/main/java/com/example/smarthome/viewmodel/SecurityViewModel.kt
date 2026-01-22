package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.smarthome.model.ActivityLog
import com.example.smarthome.model.SecurityModel
import com.example.smarthome.repo.SecurityRepo

class SecurityViewModel(private val repo: SecurityRepo) : ViewModel() {

    val state = mutableStateOf(SecurityModel())

    init {
        repo.observeSecurity { model ->
            state.value = model
        }
    }

    fun setActiveMode(mode: String) {
        update(state.value.copy(activeMode = mode))
    }

    fun setMotionDetection(enabled: Boolean) {
        update(state.value.copy(motionDetection = enabled))
    }

    fun setDoorSensors(enabled: Boolean) {
        update(state.value.copy(doorSensors = enabled))
    }

    fun setPushNotifications(enabled: Boolean) {
        update(state.value.copy(pushNotifications = enabled))
    }

    fun addActivity(description: String, timestamp: String) {
        val newActivity = ActivityLog(description, timestamp)
        val updatedActivities = listOf(newActivity) + state.value.recentActivities.take(9)
        update(state.value.copy(recentActivities = updatedActivities))
    }

    private fun update(model: SecurityModel) {
        state.value = model
        repo.updateSecurity(model)
    }
}
