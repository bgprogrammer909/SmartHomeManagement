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
    private val userId: String,
    private val repo: WaterRepo = WaterRepoImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(WaterModel())
    val state = _state.asStateFlow()

    init {
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

    fun turnOn() {
        val updated = _state.value.copy(isPumpOn = true)
        update(updated)
    }

    fun turnOff() {
        val updated = _state.value.copy(isPumpOn = false)
        update(updated)
    }

    private fun update(model: WaterModel) {
        _state.value = model
        viewModelScope.launch {
            repo.updateWater(userId, model) { success, _ ->
                if (!success) {
                    // Optional: handle failure
                }
            }
        }
    }
}
