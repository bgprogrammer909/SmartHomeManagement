package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthome.model.ClimateModel
import com.example.smarthome.repo.ClimateRepo
import com.example.smarthome.repo.ClimateRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClimateViewModel(
    private val repo: ClimateRepo = ClimateRepoImpl(),
    private val userId: String
) : ViewModel() {

    private val _state = MutableStateFlow(ClimateModel())
    val state = _state.asStateFlow()

    init {
        // Fetch initial data and listen for realtime updates
        repo.getFanRealtime(userId) { success, data ->
            if (success && data != null) {
                _state.value = data
            }
        }
    }

    fun setFanSpeed(value: Int) {
        val updated = _state.value.copy(fanSpeed = value)
        update(updated)
    }

    fun setPower(value: Boolean) {
        val updated = _state.value.copy(powerOn = value)
        update(updated)
    }

    fun setAutoMode(value: Boolean) {
        val updated = _state.value.copy(autoMode = value)
        update(updated)
    }

    fun setTemperature(value: Int) {
        val updated = _state.value.copy(temperature = value)
        update(updated)
    }

    private fun update(model: ClimateModel) {
        // Update local state immediately
        _state.value = model

        // Persist to Firebase asynchronously
        viewModelScope.launch {
            repo.updateFan(userId, model) { success, _ ->
                if (!success) {
                    // Optional: handle failure (retry, show Toast, etc.)
                }
            }
        }
    }
}
