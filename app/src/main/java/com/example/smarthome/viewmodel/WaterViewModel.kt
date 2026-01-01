package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthome.model.WaterModel
import com.example.smarthome.repo.WaterRepo
import com.example.smarthome.repo.WaterRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WaterViewModel(
    private val repo: WaterRepo = WaterRepoImpl()
) : ViewModel() {

    // Full state holding WaterModel
    private val _state = MutableStateFlow(WaterModel())
    val state: StateFlow<WaterModel> get() = _state

    init {
        // Observe Firebase updates
        repo.observeWater { model ->
            _state.value = model
        }
    }

    // Toggle pump
    fun togglePump() {
        val current = _state.value
        val updated = current.copy(isPumpOn = !current.isPumpOn)
        _state.value = updated
        repo.updateWater(updated)
    }

    // Toggle auto mode
    fun toggleAutoMode() {
        val current = _state.value
        val updated = current.copy(autoMode = !current.autoMode)
        _state.value = updated
        repo.updateWater(updated)
    }

    // Optional: directly turn on/off
    fun turnOn() {
        val updated = _state.value.copy(isPumpOn = true)
        _state.value = updated
        repo.updateWater(updated)
    }

    fun turnOff() {
        val updated = _state.value.copy(isPumpOn = false)
        _state.value = updated
        repo.updateWater(updated)
    }
}
