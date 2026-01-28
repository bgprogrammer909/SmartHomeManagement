package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smarthome.repo.EnergyRepoImpl

class EnergyViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnergyViewModel::class.java)) {
            return EnergyViewModel(EnergyRepoImpl()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
