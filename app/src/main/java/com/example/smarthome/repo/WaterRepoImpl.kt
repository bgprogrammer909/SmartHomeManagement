package com.example.smarthome.repo

import com.example.smarthome.model.EnergyModel
import com.google.firebase.database.*

class EnergyRepoImpl(
    private val userId: String
) : EnergyRepo {

    private val ref: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("energy").child(userId)

    override fun observeEnergy(onChange: (EnergyModel) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val model = snapshot.getValue(EnergyModel::class.java)
                    ?: EnergyModel()

                onChange(
                    model.copy(
                        dayData = smooth(model.dayData),
                        weekData = smooth(model.weekData),
                        monthData = smooth(model.monthData)
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun updateEnergy(model: EnergyModel) {
        ref.setValue(model)
    }

    // ---------- Graph Smoothing (EMA) ----------
    private fun smooth(
        data: List<Float>,
        alpha: Float = 0.35f
    ): List<Float> {
        if (data.isEmpty()) return data

        val result = mutableListOf(data.first())
        for (i in 1 until data.size) {
            val smoothed =
                alpha * data[i] + (1 - alpha) * result[i - 1]
            result.add(smoothed)
        }
        return result
    }
}
