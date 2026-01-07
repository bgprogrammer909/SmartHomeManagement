package com.example.smarthome.repo

import com.example.smarthome.model.WaterModel
import com.google.firebase.database.*

class WaterRepoImpl : WaterRepo {

    private var listener: ValueEventListener? = null

    private fun waterRef(userId: String) =
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
            .child("water")

    override fun getWaterRealtime(userId: String, callback: (success: Boolean, data: WaterModel?) -> Unit) {
        val ref = waterRef(userId)
        listener?.let { ref.removeEventListener(it) }

        listener = object : ValueEventListener {
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

                    callback(true, model)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(false, null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, null)
            }
        }

        ref.addValueEventListener(listener!!)
    }

    override fun updateWater(userId: String, model: WaterModel, callback: (success: Boolean, error: String?) -> Unit) {
        waterRef(userId).setValue(model)
            .addOnSuccessListener { callback(true, null) }
            .addOnFailureListener { callback(false, it.message) }
    }
}
