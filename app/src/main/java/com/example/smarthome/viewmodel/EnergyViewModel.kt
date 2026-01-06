package com.example.smarthome.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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

    fun addDayValue(value: Float) {
        val updated = state.value.dayData
            .takeLast(6)
            .plus(value)

        update(state.value.copy(dayData = updated))
    }

    fun addWeekValue(value: Float) {
        val updated = state.value.weekData
            .takeLast(6)
            .plus(value)

        update(state.value.copy(weekData = updated))
    }

    fun addMonthValue(value: Float) {
        val updated = state.value.monthData
            .takeLast(6)
            .plus(value)

        update(state.value.copy(monthData = updated))
    }

    private fun update(model: EnergyModel) {
        state.value = model
        repo.updateEnergy(model)
    }
}
