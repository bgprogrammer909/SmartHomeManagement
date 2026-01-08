package com.example.smarthome.repo

import com.example.smarthome.model.ActivityLog
import com.example.smarthome.model.SecurityModel
import com.google.firebase.database.*

class SecurityRepoImpl : SecurityRepo {

    private val ref = FirebaseDatabase.getInstance().getReference("securitySystem")

    override fun observeSecurity(onChange: (SecurityModel) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val activeMode = snapshot.child("activeMode").getValue(String::class.java) ?: "HOME"
                    val motionDetection = snapshot.child("motionDetection").getValue(Boolean::class.java) ?: true
                    val doorSensors = snapshot.child("doorSensors").getValue(Boolean::class.java) ?: true
                    val pushNotifications = snapshot.child("pushNotifications").getValue(Boolean::class.java) ?: true

                    val activities = mutableListOf<ActivityLog>()
                    snapshot.child("recentActivities").children.forEach { activitySnapshot ->
                        val description = activitySnapshot.child("description").getValue(String::class.java) ?: ""
                        val timestamp = activitySnapshot.child("timestamp").getValue(String::class.java) ?: ""
                        if (description.isNotEmpty()) {
                            activities.add(ActivityLog(description, timestamp))
                        }
                    }

                    if (activities.isEmpty()) {
                        activities.add(ActivityLog("Motion detected – Front door", "2 min ago"))
                        activities.add(ActivityLog("Door opened – Main entrance", "15 min ago"))
                        activities.add(ActivityLog("Camera triggered – Backyard", "1 hour ago"))
                    }

                    val model = SecurityModel(
                        activeMode = activeMode,
                        motionDetection = motionDetection,
                        doorSensors = doorSensors,
                        pushNotifications = pushNotifications,
                        recentActivities = activities
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

    override fun updateSecurity(model: SecurityModel) {
        ref.setValue(model)
    }
}