package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthome.model.WaterModel
import com.example.smarthome.repo.WaterRepo
import com.example.smarthome.repo.WaterRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WaterViewModel(
    private val repo: WaterRepo = WaterRepoImpl(),
    private val userId: String
) : ViewModel() {

    private val _state = MutableStateFlow(WaterModel())
    val state = _state.asStateFlow()

    init {
        // Fetch initial data and listen for realtime updates
        repo.getWaterRealtime(userId) { success, data ->
            if (success && data != null) {
                _state.value = data
            }
        }
    }

    fun togglePump() {
        val updated = _state.value.copy(isPumpOn = !_state.value.isPumpOn)
        update(updated)
    }

    fun toggleAutoMode() {
        val updated = _state.value.copy(autoMode = !_state.value.autoMode)
        update(updated)
    }

    fun setPumpOn(value: Boolean) {
        val updated = _state.value.copy(isPumpOn = value)
        update(updated)
    }

    fun setAutoMode(value: Boolean) {
        val updated = _state.value.copy(autoMode = value)
        update(updated)
    }

    fun setTodayUsage(value: Double) {
        val updated = _state.value.copy(todayUsage = value)
        update(updated)
    }

    fun setFlowRate(value: Double) {
        val updated = _state.value.copy(flowRate = value)
        update(updated)
    }

    private fun update(model: WaterModel) {
        // Update local state immediately
        _state.value = model

        // Persist to Firebase asynchronously
        viewModelScope.launch {
            repo.updateWater(userId, model) { success, _ ->
                if (!success) {
                    // Optional: handle failure (retry, show Toast, etc.)
                }
            }
        }
    }
}