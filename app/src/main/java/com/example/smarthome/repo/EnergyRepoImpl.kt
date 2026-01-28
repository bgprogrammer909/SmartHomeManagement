package com.example.smarthome.repo

import com.example.smarthome.model.EnergyModel
import com.example.smarthome.model.EnergyPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EnergyRepoImpl : EnergyRepo {

    private fun getEnergyRef(): DatabaseReference {
        val user = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("User not logged in")

        return FirebaseDatabase.getInstance()
            .getReference("users")
            .child(user.uid)
            .child("energyAnalytics")
    }

    override fun observeEnergy(onChange: (EnergyModel) -> Unit) {

        getEnergyRef().addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    onChange(EnergyModel())
                    return
                }

                val totalUsage =
                    snapshot.child("totalUsage").getValue(Double::class.java)
                        ?.toFloat() ?: 0f

                val lightsUsage =
                    snapshot.child("lightsUsage").getValue(Int::class.java) ?: 0
                val acUsage =
                    snapshot.child("acUsage").getValue(Int::class.java) ?: 0
                val waterPumpUsage =
                    snapshot.child("waterPumpUsage").getValue(Int::class.java) ?: 0
                val othersUsage =
                    snapshot.child("othersUsage").getValue(Int::class.java) ?: 0

                val estimatedBill =
                    snapshot.child("estimatedBill").getValue(Double::class.java) ?: 0.0

                val savings =
                    snapshot.child("savings").getValue(Double::class.java) ?: 0.0

                val dayData = snapshot.child("dayData").children.mapNotNull {
                    val label = it.key ?: return@mapNotNull null
                    val kw = it.getValue(Double::class.java)?.toFloat() ?: 0f
                    EnergyPoint(label, kw)
                }

                val weekData = snapshot.child("weekData").children.mapNotNull {
                    val label = it.key ?: return@mapNotNull null
                    val kw = it.getValue(Double::class.java)?.toFloat() ?: 0f
                    EnergyPoint(label, kw)
                }

                val monthData = snapshot.child("monthData").children.mapNotNull {
                    val label = it.key ?: return@mapNotNull null
                    val kw = it.getValue(Double::class.java)?.toFloat() ?: 0f
                    EnergyPoint(label, kw)
                }

                onChange(
                    EnergyModel(
                        totalUsage = totalUsage,
                        lightsUsage = lightsUsage,
                        acUsage = acUsage,
                        waterPumpUsage = waterPumpUsage,
                        othersUsage = othersUsage,
                        estimatedBill = estimatedBill,
                        savings = savings,
                        dayData = dayData,
                        weekData = weekData,
                        monthData = monthData
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    override fun updateEnergy(model: EnergyModel) {
        getEnergyRef().setValue(model)
    }
}
