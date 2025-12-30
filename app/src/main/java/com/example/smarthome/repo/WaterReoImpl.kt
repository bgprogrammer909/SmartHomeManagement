package com.example.smarthome.repo

import com.example.smarthome.model.WaterModel
import com.google.firebase.database.*

class WaterRepoImpl : WaterRepo {

    private val ref = FirebaseDatabase.getInstance().getReference("waterControl")

    override fun observeWater(onChange: (WaterModel) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val isPumpOn = snapshot.child("isPumpOn").getValue(Boolean::class.java) ?: false
                    val autoMode = snapshot.child("autoMode").getValue(Boolean::class.java) ?: false

                    val todayUsage = snapshot.child("todayUsage").getValue(Any::class.java).let {
                        when (it) {
                            is Double -> it
                            is Long -> it.toDouble()
                            is Int -> it.toDouble()
                            is String -> it.toDoubleOrNull() ?: 0.0
                            else -> 0.0
                        }
                    }

                    val flowRate = snapshot.child("flowRate").getValue(Any::class.java).let {
                        when (it) {
                            is Double -> it
                            is Long -> it.toDouble()
                            is Int -> it.toDouble()
                            is String -> it.toDoubleOrNull() ?: 0.0
                            else -> 0.0
                        }
                    }

                    val energySavings = snapshot.child("energySavings").getValue(Any::class.java).let {
                        when (it) {
                            is Long -> it.toInt()
                            is Int -> it
                            is String -> it.toIntOrNull() ?: 15
                            else -> 15
                        }
                    }

                    val model = WaterModel(
                        isPumpOn = isPumpOn,
                        autoMode = autoMode,
                        todayUsage = todayUsage,
                        flowRate = flowRate,
                        energySavings = energySavings
                    )

                    onChange(model)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    override fun updateWater(model: WaterModel) {
        ref.setValue(model)
    }
}