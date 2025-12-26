package com.example.smarthome.repo

import android.util.Log
import com.example.smarthome.model.DoorModel
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DoorRepositoryImpl : DoorRepository {

    private val dbRef = FirebaseDatabase.getInstance().getReference("doors")
    private val _doorsFlow = MutableStateFlow(DoorModel())
    override fun getDoorsRealtime(): StateFlow<DoorModel> = _doorsFlow

    init {
        // Listen for changes and create default if not exist
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mainLocked = snapshot.child("mainDoorLocked").getValue(Boolean::class.java)
                val homeLocked = snapshot.child("homeDoorLocked").getValue(Boolean::class.java)

                if (mainLocked == null || homeLocked == null) {
                    // If data doesn't exist, create default
                    val default = DoorModel()
                    dbRef.setValue(default)
                    _doorsFlow.value = default
                } else {
                    _doorsFlow.value = DoorModel(mainLocked, homeLocked)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DoorRepo", "Firebase error: ${error.message}")
            }
        })
    }

    override fun toggleMainDoor() {
        val current = _doorsFlow.value
        val updated = current.copy(mainDoorLocked = !current.mainDoorLocked)
        dbRef.setValue(updated)
    }

    override fun toggleHomeDoor() {
        val current = _doorsFlow.value
        val updated = current.copy(homeDoorLocked = !current.homeDoorLocked)
        dbRef.setValue(updated)
    }

    override fun lockAll() {
        val updated = DoorModel(true, true)
        dbRef.setValue(updated)
    }

    override fun unlockAll() {
        val updated = DoorModel(false, false)
        dbRef.setValue(updated)
    }
}
