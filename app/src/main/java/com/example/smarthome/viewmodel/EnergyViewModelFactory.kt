package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EnergyViewModelFactory(
    private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnergyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnergyViewModel(userId = userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}