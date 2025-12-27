package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthome.repo.WaterRepository
import com.example.smarthome.repo.WaterRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WaterViewModel(
    private val repo: WaterRepository = WaterRepositoryImpl()
) : ViewModel() {

    private val _waterOn = MutableStateFlow(false)
    val waterOn: StateFlow<Boolean> get() = _waterOn

    private val _autoMode = MutableStateFlow(false)
    val autoMode: StateFlow<Boolean> get() = _autoMode

    init {
        viewModelScope.launch {
            repo.getWaterRealtime().collect { water ->
                _waterOn.value = water.waterOn
                _autoMode.value = water.automaticMode
            }
        }
    }

    fun togglePump() = repo.togglePump()
    fun toggleAutoMode() = repo.toggleAutoMode()
    fun turnOn() = repo.turnOn()
    fun turnOff() = repo.turnOff()
}
