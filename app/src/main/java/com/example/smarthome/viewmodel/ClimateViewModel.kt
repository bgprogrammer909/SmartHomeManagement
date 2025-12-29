package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smarthome.model.ClimateModel
import com.example.smarthome.repo.ClimateRepo
import com.example.smarthome.repo.ClimateRepoImpl
import com.example.smarthome.util.CurrentUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClimateViewModel(
    private val repo: ClimateRepo = ClimateRepoImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(ClimateModel())
    val state = _state.asStateFlow()

    init {
        CurrentUser.userId?.let { userId ->
            repo.getFanRealtime(userId) { success, data ->
                if (success && data != null) {
                    _state.value = data
                }
            }
        }
    }

    fun setFanSpeed(value: Int) {
        update(_state.value.copy(fanSpeed = value))
    }

    fun setPower(value: Boolean) {
        update(_state.value.copy(powerOn = value))
    }

    fun setAutoMode(value: Boolean) {
        update(_state.value.copy(autoMode = value))
    }

    fun setTemperature(value: Int) {
        update(_state.value.copy(temperature = value))
    }

    private fun update(model: ClimateModel) {
        _state.value = model
        CurrentUser.userId?.let { userId ->
            repo.updateFan(userId, model) { _, _ -> }
        }
    }
}
