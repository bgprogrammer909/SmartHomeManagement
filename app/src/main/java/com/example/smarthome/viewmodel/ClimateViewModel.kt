package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.smarthome.model.ClimateModel
import com.example.smarthome.repo.ClimateRepo
import com.example.smarthome.repo.ClimateRepoImpl

class ClimateViewModel(
    private val repo: ClimateRepo = ClimateRepoImpl()
) : ViewModel() {

    val state = mutableStateOf(ClimateModel())

    init {
        repo.observeClimate {
            state.value = it
        }
    }

    fun setFanSpeed(value: Int) {
        update(state.value.copy(fanSpeed = value))
    }

    fun setPower(value: Boolean) {
        update(state.value.copy(powerOn = value))
    }

    fun setAutoMode(value: Boolean) {
        update(state.value.copy(autoMode = value))
    }

    fun setTemperature(value: Int) {
        update(state.value.copy(temperature = value))
    }

    private fun update(model: ClimateModel) {
        state.value = model
        repo.updateClimate(model)
    }
}
