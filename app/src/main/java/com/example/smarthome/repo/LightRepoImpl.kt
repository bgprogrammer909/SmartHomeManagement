package com.example.smarthome.repo

import android.util.Log
import com.example.smarthome.model.LightModel
import com.google.firebase.database.*

// Implementation of LightRepo using Firebase Realtime Database
class LightRepoImpl : LightRepo {

    private val dbRef = FirebaseDatabase.getInstance().getReference("lights") // Reference to "lights" node
    private var listener: ValueEventListener? = null

    /**
     * Listen for real-time updates to the lights.
     * Returns the current state via callback.
     */
    override fun getLightsRealtime(callback: (success: Boolean, data: LightModel?) -> Unit) {
        // Remove previous listener if exists
        listener?.let { dbRef.removeEventListener(it) }

        // Create a new listener
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

        // Add listener to Firebase
        dbRef.addValueEventListener(listener as ValueEventListener)
    }

    /**
     * Update a specific light's on/off state and brightness.
     */
    override fun updateLight(lightNumber: Int, isOn: Boolean, brightness: Float, callback: (Boolean, String?) -> Unit) {
        val updates = mapOf(
            "light${lightNumber}On" to isOn,         // e.g., light1On or light2On
            "light${lightNumber}Brightness" to brightness // e.g., light1Brightness
        )
        dbRef.updateChildren(updates)
            .addOnSuccessListener { callback(true, null) }
            .addOnFailureListener { callback(false, it.message) }
    }
}
