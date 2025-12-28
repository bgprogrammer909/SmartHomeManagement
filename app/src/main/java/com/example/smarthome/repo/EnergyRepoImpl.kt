package com.example.smarthome.repo

import com.example.smarthome.model.EnergyModel
import com.google.firebase.database.*

class EnergyRepoImpl : EnergyRepo {

    private val ref = FirebaseDatabase.getInstance().getReference("energyAnalytics")

    override fun observeEnergy(onChange: (EnergyModel) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val totalUsage = snapshot.child("totalUsage").getValue(Any::class.java).toDoubleOrDefault(120.0)
                    val percentageDiff = snapshot.child("percentageDiff").getValue(Any::class.java).toIntOrDefault(12)
                    val todayUsage = snapshot.child("todayUsage").getValue(Any::class.java).toDoubleOrDefault(18.0)
                    val weekUsage = snapshot.child("weekUsage").getValue(Any::class.java).toDoubleOrDefault(120.0)
                    val monthUsage = snapshot.child("monthUsage").getValue(Any::class.java).toDoubleOrDefault(485.0)

                    val dayData = snapshot.child("dayData").children.mapNotNull {
                        it.getValue(Any::class.java).toFloatOrDefault(0f)
                    }.ifEmpty { listOf(20f, 40f, 35f, 50f, 45f, 30f, 60f) }

                    val weekData = snapshot.child("weekData").children.mapNotNull {
                        it.getValue(Any::class.java).toFloatOrDefault(0f)
                    }.ifEmpty { listOf(90f, 95f, 80f, 88f, 92f, 100f, 97f) }

                    val monthData = snapshot.child("monthData").children.mapNotNull {
                        it.getValue(Any::class.java).toFloatOrDefault(0f)
                    }.ifEmpty { listOf(60f, 65f, 70f, 80f, 75f, 85f, 95f) }

                    val lightsUsage = snapshot.child("lightsUsage").getValue(Any::class.java).toIntOrDefault(28)
                    val acUsage = snapshot.child("acUsage").getValue(Any::class.java).toIntOrDefault(42)
                    val waterPumpUsage = snapshot.child("waterPumpUsage").getValue(Any::class.java).toIntOrDefault(78)
                    val othersUsage = snapshot.child("othersUsage").getValue(Any::class.java).toIntOrDefault(18)
                    val estimatedBill = snapshot.child("estimatedBill").getValue(Any::class.java).toDoubleOrDefault(24.50)
                    val savings = snapshot.child("savings").getValue(Any::class.java).toDoubleOrDefault(3.20)

                    val model = EnergyModel(
                        totalUsage = totalUsage,
                        percentageDiff = percentageDiff,
                        todayUsage = todayUsage,
                        weekUsage = weekUsage,
                        monthUsage = monthUsage,
                        dayData = dayData,
                        weekData = weekData,
                        monthData = monthData,
                        lightsUsage = lightsUsage,
                        acUsage = acUsage,
                        waterPumpUsage = waterPumpUsage,
                        othersUsage = othersUsage,
                        estimatedBill = estimatedBill,
                        savings = savings
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

    override fun updateEnergy(model: EnergyModel) {
        ref.setValue(model)
    }

    // Helper extension functions
    private fun Any?.toDoubleOrDefault(default: Double): Double {
        return when (this) {
            is Double -> this
            is Long -> this.toDouble()
            is Int -> this.toDouble()
            is String -> this.toDoubleOrNull() ?: default
            else -> default
        }
    }

    private fun Any?.toIntOrDefault(default: Int): Int {
        return when (this) {
            is Long -> this.toInt()
            is Int -> this
            is String -> this.toIntOrNull() ?: default
            else -> default
        }
    }

    private fun Any?.toFloatOrDefault(default: Float): Float {
        return when (this) {
            is Double -> this.toFloat()
            is Long -> this.toFloat()
            is Int -> this.toFloat()
            is String -> this.toFloatOrNull() ?: default
            else -> default
        }
    }
}