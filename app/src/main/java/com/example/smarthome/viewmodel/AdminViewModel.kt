package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smarthome.model.AdminModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().getReference("users")

    private val _users = MutableStateFlow<List<AdminModel>>(emptyList())
    val users = _users.asStateFlow()

    fun fetchAllUsers() {
        database.get().addOnSuccessListener { snapshot ->
            val userList = snapshot.children.mapNotNull { child ->
                val id = child.key ?: return@mapNotNull null
                val email = child.child("email").getValue(String::class.java) ?: ""
                val password = child.child("password").getValue(String::class.java) ?: ""
                val isActive = child.child("isActive").getValue(Boolean::class.java) ?: true

                AdminModel(
                    id = id,
                    email = email,
                    password = password,
                    isActive = isActive
                )
            }
            _users.value = userList
        }
    }

    fun addUser(email: String, password: String) {

        // Generate unique numeric ID
        val numericIds = _users.value.mapNotNull { it.id.toIntOrNull() }
        val nextId = (numericIds.maxOrNull() ?: 9999) + 1
        val userId = nextId.toString()

        // Default lights
        val lights = mapOf(
            "light1On" to true,
            "light1Brightness" to 50f,
            "light2On" to true,
            "light2Brightness" to 50f
        )

        // Default climate control
        val climateControl = mapOf(
            "autoMode" to false,
            "fanSpeed" to 2,
            "powerOn" to false,
            "temperature" to 28
        )

        // Default doors
        val doors = mapOf(
            "homeDoorLocked" to false,
            "mainDoorLocked" to false
        )

        // Default water control
        val waterControl = mapOf(
            "autoMode" to true,
            "energySavings" to 15,
            "flowRate" to 0,
            "pumpOn" to false,
            "todayUsage" to 0
        )

        // Full user map
        val userMap = mapOf(
            "email" to email,
            "password" to password,
            "isActive" to true,
            "lights" to lights,
            "climateControl" to climateControl,
            "doors" to doors,
            "waterControl" to waterControl
        )

        database.child(userId).setValue(userMap)
            .addOnSuccessListener { fetchAllUsers() }
    }

    fun updateUserStatus(userId: String, isActive: Boolean) {
        database.child(userId).child("isActive").setValue(isActive)
            .addOnSuccessListener { fetchAllUsers() }
    }

    fun updatePassword(userId: String, password: String) {
        database.child(userId).child("password").setValue(password)
            .addOnSuccessListener { fetchAllUsers() }
    }
}
