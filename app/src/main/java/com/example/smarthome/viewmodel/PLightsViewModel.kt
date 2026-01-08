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
        // Fetch initial data and listen for realtime updates
        repo.getLightsRealtime(userId) { success, data ->
            if (success && data != null) {
                _lights.value = data
            }
        }
    }

    fun toggleLight(lightNum: Int, isOn: Boolean) {
        val updated = when (lightNum) {
            1 -> _lights.value.copy(light1On = isOn)
            2 -> _lights.value.copy(light2On = isOn)
            else -> return
        }
        update(updated)
    }

    fun changeBrightness(lightNum: Int, brightness: Float) {
        val updated = when (lightNum) {
            1 -> _lights.value.copy(light1Brightness = brightness)
            2 -> _lights.value.copy(light2Brightness = brightness)
            else -> return
        }
        update(updated)
    }

    fun turnOnAll() {
        val updated = _lights.value.copy(
            light1On = true,
            light2On = true,
            isOn = true
        )
        update(updated)
    }

    fun turnOffAll() {
        val updated = _lights.value.copy(
            light1On = false,
            light2On = false,
            isOn = false
        )
        update(updated)
    }

    private fun update(model: LightModel) {
        // Update local state immediately
        _lights.value = model

        // Persist to Firebase asynchronously
        viewModelScope.launch {
            repo.updateLights(userId, model) { success, _ ->
                if (!success) {
                    // Optional: handle failure (retry, show Toast, etc.)
                }
            }
        }
    }
}