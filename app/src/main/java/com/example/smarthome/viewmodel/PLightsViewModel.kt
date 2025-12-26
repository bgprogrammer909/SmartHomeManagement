package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smarthome.model.LightModel
import com.example.smarthome.repo.PLightRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PLightsViewModel(
    private val repo: PLightRepo,
    private val userId: String
) : ViewModel() {

    private val _lights = MutableStateFlow(LightModel())
    val lights = _lights.asStateFlow()

    init {
        repo.getLightsRealtime(userId) { success, data ->
            if (success && data != null) {
                _lights.value = data
            }
        }
    }

    fun toggleLight(lightNumber: Int, isOn: Boolean) {
        val current = _lights.value

        val updated = when (lightNumber) {
            1 -> current.copy(light1On = isOn)
            2 -> current.copy(light2On = isOn)
            else -> current
        }

        _lights.value = updated

        val brightness =
            if (lightNumber == 1) current.light1Brightness else current.light2Brightness

        repo.updateLight(userId, lightNumber, isOn, brightness) { _, _ -> }
    }

    fun changeBrightness(lightNumber: Int, brightness: Float) {
        val current = _lights.value

        val updated = when (lightNumber) {
            1 -> current.copy(light1Brightness = brightness)
            2 -> current.copy(light2Brightness = brightness)
            else -> current
        }

        _lights.value = updated

        val isOn =
            if (lightNumber == 1) current.light1On else current.light2On

        repo.updateLight(userId, lightNumber, isOn, brightness) { _, _ -> }
    }

    fun turnOnAll() {
        val current = _lights.value
        _lights.value = current.copy(light1On = true, light2On = true)

        repo.updateLight(userId, 1, true, current.light1Brightness) { _, _ -> }
        repo.updateLight(userId, 2, true, current.light2Brightness) { _, _ -> }
    }

    fun turnOffAll() {
        val current = _lights.value
        _lights.value = current.copy(light1On = false, light2On = false)

        repo.updateLight(userId, 1, false, current.light1Brightness) { _, _ -> }
        repo.updateLight(userId, 2, false, current.light2Brightness) { _, _ -> }
    }
}
