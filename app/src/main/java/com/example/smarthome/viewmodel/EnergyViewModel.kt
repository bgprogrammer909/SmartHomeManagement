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
}
