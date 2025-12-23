package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthome.repo.DoorRepository
import com.example.smarthome.repo.DoorRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DoorViewModel(private val repo: DoorRepository = DoorRepositoryImpl()) : ViewModel() {

    private val _mainDoor = MutableStateFlow(true)
    val mainDoor: StateFlow<Boolean> get() = _mainDoor

    private val _homeDoor = MutableStateFlow(true)
    val homeDoor: StateFlow<Boolean> get() = _homeDoor

    init {
        viewModelScope.launch {
            repo.getDoorsRealtime().collect { doors ->
                _mainDoor.value = doors.mainDoorLocked
                _homeDoor.value = doors.homeDoorLocked
            }
        }
    }

    fun toggleMainDoor() = repo.toggleMainDoor()
    fun toggleHomeDoor() = repo.toggleHomeDoor()
    fun lockAll() = repo.lockAll()
    fun unlockAll() = repo.unlockAll()
}
