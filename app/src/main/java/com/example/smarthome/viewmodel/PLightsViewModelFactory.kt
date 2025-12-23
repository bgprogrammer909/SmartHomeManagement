package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smarthome.repo.PLightRepo

class PLightsViewModelFactory(
    private val repo: PLightRepo,
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PLightsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PLightsViewModel(repo, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
