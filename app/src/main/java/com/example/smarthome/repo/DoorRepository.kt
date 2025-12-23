package com.example.smarthome.repo

import com.example.smarthome.model.DoorModel
import kotlinx.coroutines.flow.StateFlow

interface DoorRepository {

    // Observe doors in real-time
    fun getDoorsRealtime(): StateFlow<DoorModel>

    // Toggle doors
    fun toggleMainDoor()
    fun toggleHomeDoor()
    fun lockAll()
    fun unlockAll()
}
