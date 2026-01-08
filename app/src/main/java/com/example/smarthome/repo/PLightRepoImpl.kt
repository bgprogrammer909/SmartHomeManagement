package com.example.smarthome.repo

import com.example.smarthome.model.LightModel
import com.google.firebase.database.*

class PLightRepoImpl : PLightRepo {

    private var listener: ValueEventListener? = null

    private fun lightsRef(userId: String) =
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
            .child("lights")

    override fun getLightsRealtime(userId: String, callback: (success: Boolean, data: LightModel?) -> Unit) {
        val ref = lightsRef(userId)
        listener?.let { ref.removeEventListener(it) }

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val light1On = snapshot.child("light1On").getValue(Boolean::class.java) ?: false
                    val light2On = snapshot.child("light2On").getValue(Boolean::class.java) ?: false
                    val light1Brightness = snapshot.child("light1Brightness").getValue(Float::class.java) ?: 50f
                    val light2Brightness = snapshot.child("light2Brightness").getValue(Float::class.java) ?: 50f
                    val isOn = snapshot.child("isOn").getValue(Boolean::class.java) ?: true

                    callback(true, LightModel(light1On, light2On, light1Brightness, light2Brightness, isOn))
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

    override fun updateLights(userId: String, model: LightModel, callback: (success: Boolean, error: String?) -> Unit) {
        lightsRef(userId).setValue(model)
            .addOnSuccessListener { callback(true, null) }
            .addOnFailureListener { callback(false, it.message) }
    }
}