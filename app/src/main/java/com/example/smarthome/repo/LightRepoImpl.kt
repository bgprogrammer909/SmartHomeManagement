package com.example.smarthome.repo

import android.util.Log
import com.example.smarthome.model.LightModel
import com.google.firebase.database.*

class LightRepoImpl : LightRepo {

    private val dbRef = FirebaseDatabase.getInstance().getReference("lights")
    private var listener: ValueEventListener? = null

    override fun getLightsRealtime(callback: (success: Boolean, data: LightModel?) -> Unit) {
        listener?.let { dbRef.removeEventListener(it) }

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val light1On = snapshot.child("light1On").getValue(Boolean::class.java) ?: true
                    val light2On = snapshot.child("light2On").getValue(Boolean::class.java) ?: true
                    val light1Brightness = snapshot.child("light1Brightness").getValue(Number::class.java)?.toFloat() ?: 50f
                    val light2Brightness = snapshot.child("light2Brightness").getValue(Number::class.java)?.toFloat() ?: 50f

                    callback(true, LightModel(light1On, light2On, light1Brightness, light2Brightness))
                } catch (e: Exception) {
                    Log.e("Firebase", "Failed to parse lights: ${e.message}")
                    callback(false, null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Database error: ${error.message}")
                callback(false, null)
            }
        }

        dbRef.addValueEventListener(listener as ValueEventListener)
    }

    override fun updateLight(lightNumber: Int, isOn: Boolean, brightness: Float, callback: (Boolean, String?) -> Unit) {
        val updates = mapOf(
            "light${lightNumber}On" to isOn,
            "light${lightNumber}Brightness" to brightness
        )
        dbRef.updateChildren(updates)
            .addOnSuccessListener { callback(true, null) }
            .addOnFailureListener { callback(false, it.message) }
    }
}
