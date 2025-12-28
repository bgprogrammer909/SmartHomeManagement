package com.example.smarthome.repo

import android.util.Log
import com.example.smarthome.model.WaterModel
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WaterRepositoryImpl : WaterRepository {

    private val dbRef = FirebaseDatabase.getInstance().getReference("water")
    private val _waterFlow = MutableStateFlow(WaterModel())

    override fun getWaterRealtime(): StateFlow<WaterModel> = _waterFlow

    init {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val waterOn = snapshot.child("waterOn").getValue(Boolean::class.java)
                val auto = snapshot.child("automaticMode").getValue(Boolean::class.java)

                if (waterOn == null || auto == null) {
                    // 🔥 Create default if node missing
                    val default = WaterModel()
                    dbRef.setValue(default)
                    _waterFlow.value = default
                } else {
                    _waterFlow.value = WaterModel(waterOn, auto)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("WaterRepo", "Firebase error: ${error.message}")
            }
        })
    }

    override fun togglePump() {
        val current = _waterFlow.value
        dbRef.setValue(current.copy(waterOn = !current.waterOn))
    }

    override fun toggleAutoMode() {
        val current = _waterFlow.value
        dbRef.setValue(current.copy(automaticMode = !current.automaticMode))
    }

    override fun turnOn() {
        val current = _waterFlow.value
        dbRef.setValue(current.copy(waterOn = true))
    }

    override fun turnOff() {
        val current = _waterFlow.value
        dbRef.setValue(current.copy(waterOn = false))
    }
}
