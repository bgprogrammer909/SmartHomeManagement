package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.smarthome.model.WaterModel
import com.example.smarthome.repo.WaterRepo
import com.example.smarthome.repo.WaterRepoImpl

class WaterViewModel(
    private val repo: WaterRepo = WaterRepoImpl()
) : ViewModel() {

    val state = mutableStateOf(WaterModel())

    init {
        repo.observeWater {
            state.value = it
        }
    }

    fun setPumpOn(value: Boolean) {
        update(state.value.copy(isPumpOn = value))
    }

    fun setAutoMode(value: Boolean) {
        update(state.value.copy(autoMode = value))
    }

    fun setTodayUsage(value: Double) {
        update(state.value.copy(todayUsage = value))
    }

    fun setFlowRate(value: Double) {
        update(state.value.copy(flowRate = value))
    }

    private fun update(model: WaterModel) {
        state.value = model
        repo.updateWater(model)
    }
}