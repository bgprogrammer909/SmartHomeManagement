package com.example.smarthome.repo

import android.util.Log
import com.example.smarthome.model.LightModel
import com.google.firebase.database.*

class PLightRepoImpl : PLightRepo {

    private var listener: ValueEventListener? = null

    private fun lightsRef(userId: String) =
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
            .child("lights")

    override fun getLightsRealtime(
        userId: String,
        callback: (success: Boolean, data: LightModel?) -> Unit
    ) {
        val dbRef = lightsRef(userId)

        listener?.let { dbRef.removeEventListener(it) }

        listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val light1 = snapshot.child("light1")
                    val light2 = snapshot.child("light2")

                    val model = LightModel(
                        light1On = light1.child("isOn").getValue(Boolean::class.java) ?: true,
                        light2On = light2.child("isOn").getValue(Boolean::class.java) ?: true,
                        light1Brightness = light1.child("brightness").getValue(Number::class.java)?.toFloat() ?: 50f,
                        light2Brightness = light2.child("brightness").getValue(Number::class.java)?.toFloat() ?: 50f
                    )

                    callback(true, model)

                } catch (e: Exception) {
                    Log.e("Firebase", "Parse error: ${e.message}")
                    callback(false, null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", error.message)
                callback(false, null)
            }
        }

        dbRef.addValueEventListener(listener!!)
    }

    override fun updateLight(
        userId: String,
        lightNumber: Int,
        isOn: Boolean,
        brightness: Float,
        callback: (Boolean, String?) -> Unit
    ) {
        val lightKey = "light$lightNumber"

        val updates = mapOf(
            "isOn" to isOn,
            "brightness" to brightness
        )

        lightsRef(userId)
            .child(lightKey)
            .updateChildren(updates)
            .addOnSuccessListener { callback(true, null) }
            .addOnFailureListener { callback(false, it.message) }
    }
}
