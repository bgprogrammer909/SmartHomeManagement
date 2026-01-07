package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DoorViewModelFactory(
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DoorViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
