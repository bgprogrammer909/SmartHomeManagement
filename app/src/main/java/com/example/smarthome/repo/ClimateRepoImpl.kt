package com.example.smarthome.repo

import com.example.smarthome.model.ClimateModel
import com.google.firebase.database.*

class ClimateRepoImpl : ClimateRepo {

    private var listener: ValueEventListener? = null

    private fun fanRef(userId: String) =
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
            .child("fan")

    override fun getFanRealtime(userId: String, callback: (success: Boolean, data: ClimateModel?) -> Unit) {
        val ref = fanRef(userId)
        listener?.let { ref.removeEventListener(it) }

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val fanSpeed = snapshot.child("fanSpeed").getValue(Int::class.java) ?: 0
                    val temperature = snapshot.child("temperature").getValue(Int::class.java) ?: 28
                    val powerOn = snapshot.child("powerOn").getValue(Boolean::class.java) ?: true
                    val autoMode = snapshot.child("autoMode").getValue(Boolean::class.java) ?: false

                    callback(true, ClimateModel(fanSpeed, powerOn, autoMode, temperature))
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

    override fun updateFan(userId: String, model: ClimateModel, callback: (success: Boolean, error: String?) -> Unit) {
        fanRef(userId).setValue(model)
            .addOnSuccessListener { callback(true, null) }
            .addOnFailureListener { callback(false, it.message) }
    }
}
