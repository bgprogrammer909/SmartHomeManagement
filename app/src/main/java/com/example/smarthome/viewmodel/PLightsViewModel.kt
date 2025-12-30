package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthome.model.LightModel
import com.example.smarthome.repo.PLightRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PLightsViewModel(
    private val repo: PLightRepo,
    private val userId: String
) : ViewModel() {

    private val _lights = MutableStateFlow(LightModel())
    val lights = _lights.asStateFlow()

    init {
        // Fetch lights from Firebase on load
        repo.getLightsRealtime(userId) { success, data ->
            if (success && data != null) {
                _lights.value = data
            }
        }
    }

    fun toggleLight(lightNumber: Int, isOn: Boolean) {
        viewModelScope.launch {
            val updated = when (lightNumber) {
                1 -> _lights.value.copy(light1On = isOn)
                2 -> _lights.value.copy(light2On = isOn)
                else -> _lights.value
            }
            _lights.value = updated

            val brightness = if (lightNumber == 1) updated.light1Brightness else updated.light2Brightness
            repo.updateLight(userId, lightNumber, isOn, brightness) { success, _ ->
                if (!success) {
                    // Revert if update fails
                    _lights.value = _lights.value.copy(
                        light1On = if (lightNumber == 1) !_lights.value.light1On else _lights.value.light1On,
                        light2On = if (lightNumber == 2) !_lights.value.light2On else _lights.value.light2On
                    )
                }
            }
        }
    }

    fun changeBrightness(lightNumber: Int, brightness: Float) {
        viewModelScope.launch {
            val updated = when (lightNumber) {
                1 -> _lights.value.copy(light1Brightness = brightness)
                2 -> _lights.value.copy(light2Brightness = brightness)
                else -> _lights.value
            }
            _lights.value = updated

            val isOn = if (lightNumber == 1) updated.light1On else updated.light2On
            repo.updateLight(userId, lightNumber, isOn, brightness) { _, _ -> }
        }
    }

    fun turnOnAll() {
        viewModelScope.launch {
            val updated = _lights.value.copy(light1On = true, light2On = true)
            _lights.value = updated

            repo.updateLight(userId, 1, true, updated.light1Brightness) { _, _ -> }
            repo.updateLight(userId, 2, true, updated.light2Brightness) { _, _ -> }
        }
    }

    fun turnOffAll() {
        viewModelScope.launch {
            val updated = _lights.value.copy(light1On = false, light2On = false)
            _lights.value = updated

            repo.updateLight(userId, 1, false, updated.light1Brightness) { _, _ -> }
            repo.updateLight(userId, 2, false, updated.light2Brightness) { _, _ -> }
        }
    }
}
