package com.example.smarthome.repo

import com.example.smarthome.model.ClimateModel
import com.google.firebase.database.*

class ClimateRepoImpl : ClimateRepo {

    private val ref = FirebaseDatabase.getInstance().getReference("climateControl")

    override fun observeClimate(onChange: (ClimateModel) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    // Manual parsing to handle string/int conversion
                    val fanSpeed = snapshot.child("fanSpeed").getValue(Any::class.java).let {
                        when (it) {
                            is Long -> it.toInt()
                            is Int -> it
                            is String -> it.toIntOrNull() ?: 0
                            else -> 0
                        }
                    }

                    val temperature = snapshot.child("temperature").getValue(Any::class.java).let {
                        when (it) {
                            is Long -> it.toInt()
                            is Int -> it
                            is String -> it.toIntOrNull() ?: 28
                            else -> 28
                        }
                    }

                    val powerOn = snapshot.child("powerOn").getValue(Boolean::class.java) ?: true
                    val autoMode = snapshot.child("autoMode").getValue(Boolean::class.java) ?: false

                    val model = ClimateModel(
                        fanSpeed = fanSpeed,
                        powerOn = powerOn,
                        autoMode = autoMode,
                        temperature = temperature
                    )

                    onChange(model)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun updateClimate(model: ClimateModel) {
        ref.setValue(model)
    }
}