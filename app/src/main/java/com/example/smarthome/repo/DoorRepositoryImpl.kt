package com.example.smarthome.repo

import android.util.Log
import com.example.smarthome.model.DoorModel
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DoorRepositoryImpl(
    userId: String
) : DoorRepository {

    private val ref = FirebaseDatabase.getInstance()
        .getReference("users")
        .child(userId)
        .child("doors")

    private val _doors = MutableStateFlow(DoorModel())
    override fun observeDoors(): StateFlow<DoorModel> = _doors

    init {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val model = snapshot.getValue(DoorModel::class.java)
                if (model == null) {
                    // create only ONCE if missing
                    ref.setValue(DoorModel())
                } else {
                    _doors.value = model
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DoorRepo", error.message)
            }
        })
    }

    override fun toggleMainDoor() {
        val updated = _doors.value.copy(
            mainDoorLocked = !_doors.value.mainDoorLocked
        )
        ref.setValue(updated)
    }

    override fun toggleHomeDoor() {
        val updated = _doors.value.copy(
            homeDoorLocked = !_doors.value.homeDoorLocked
        )
        ref.setValue(updated)
    }

    override fun lockAll() {
        ref.setValue(DoorModel(true, true))
    }

    override fun unlockAll() {
        ref.setValue(DoorModel(false, false))
    }
}
