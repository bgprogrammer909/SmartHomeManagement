package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthome.repo.DoorRepository
import com.example.smarthome.repo.DoorRepositoryImpl
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoorViewModel(
    userId: String
) : ViewModel() {

    private val repo: DoorRepository = DoorRepositoryImpl(userId)

    val doors: StateFlow<com.example.smarthome.model.DoorModel> =
        repo.observeDoors()

    fun toggleMainDoor() = repo.toggleMainDoor()
    fun toggleHomeDoor() = repo.toggleHomeDoor()
    fun lockAll() = repo.lockAll()
    fun unlockAll() = repo.unlockAll()
}
