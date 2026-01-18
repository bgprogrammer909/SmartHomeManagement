package com.example.smarthome.repo

import com.example.smarthome.model.DoorModel
import kotlinx.coroutines.flow.StateFlow

interface DoorRepository {
    fun observeDoors(): StateFlow<DoorModel>
    fun toggleMainDoor()
    fun toggleHomeDoor()
    fun lockAll()
    fun unlockAll()
}
