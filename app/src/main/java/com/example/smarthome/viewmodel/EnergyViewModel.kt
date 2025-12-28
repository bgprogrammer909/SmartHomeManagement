package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.smarthome.model.EnergyModel
import com.example.smarthome.repo.EnergyRepo
import com.example.smarthome.repo.EnergyRepoImpl

class EnergyViewModel(
    private val repo: EnergyRepo = EnergyRepoImpl()
) : ViewModel() {

    val state = mutableStateOf(EnergyModel())

    init {
        repo.observeEnergy {
            state.value = it
        }
    }

    fun updateTotalUsage(value: Double) {
        update(state.value.copy(totalUsage = value))
    }

    fun updateTodayUsage(value: Double) {
        update(state.value.copy(todayUsage = value))
    }

    fun updateWeekUsage(value: Double) {
        update(state.value.copy(weekUsage = value))
    }

    fun updateMonthUsage(value: Double) {
        update(state.value.copy(monthUsage = value))
    }

    fun updateLightsUsage(value: Int) {
        update(state.value.copy(lightsUsage = value))
    }

    fun updateAcUsage(value: Int) {
        update(state.value.copy(acUsage = value))
    }

    fun updateWaterPumpUsage(value: Int) {
        update(state.value.copy(waterPumpUsage = value))
    }

    fun updateOthersUsage(value: Int) {
        update(state.value.copy(othersUsage = value))
    }

    private fun update(model: EnergyModel) {
        state.value = model
        repo.updateEnergy(model)
    }
}