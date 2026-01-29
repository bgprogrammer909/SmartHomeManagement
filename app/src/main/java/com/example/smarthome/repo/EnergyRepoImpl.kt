package com.example.smarthome.repo

import com.google.firebase.database.*

class EnergyRepoImpl : EnergyRepo {

    private var listener: ValueEventListener? = null

    private fun energyRef(userId: String) =
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
            .child("energyAnalysis")

    override fun getEnergyHistoryRealtime(
        userId: String,
        callback: (success: Boolean, history: Map<String, Float>?) -> Unit
    ) {
        val ref = energyRef(userId)
        listener?.let { ref.removeEventListener(it) }

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val historyMap = mutableMapOf<String, Float>()

                    // Read all date:kwh pairs
                    snapshot.children.forEach { dateSnapshot ->
                        val date = dateSnapshot.key ?: return@forEach
                        val kwh = dateSnapshot.getValue(Float::class.java) ?:
                        dateSnapshot.getValue(Double::class.java)?.toFloat() ?: 0f
                        historyMap[date] = kwh
                    }

                    callback(true, historyMap)
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

    fun removeListener(userId: String) {
        listener?.let {
            energyRef(userId).removeEventListener(it)
            listener = null
        }
    }
}
