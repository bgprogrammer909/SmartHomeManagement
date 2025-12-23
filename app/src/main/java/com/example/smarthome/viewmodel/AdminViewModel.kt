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

    init {
        fetchAllUsers()
    }

    fun fetchAllUsers() {
        database.get().addOnSuccessListener { snapshot ->
            val userList = snapshot.children.mapNotNull { child ->
                val id = child.key ?: return@mapNotNull null
                val password = child.child("password").getValue(String::class.java) ?: ""
                val isActive = child.child("isActive").getValue(Boolean::class.java) ?: true
                AdminModel(id = id, password = password, isActive = isActive)
            }
            _users.value = userList
        }
    }

    fun addUser(user: AdminModel) {
        val userId = user.id

        // Default lights for new user
        val defaultLights = mapOf(
            "light1On" to true,
            "light1Brightness" to 50f,
            "light2On" to true,
            "light2Brightness" to 50f
        )

        // Full user map
        val userMap = mapOf(
            "password" to user.password,
            "isActive" to (user.isActive ?: true),
            "lights" to defaultLights
        )

        database.child(userId).setValue(userMap)
            .addOnSuccessListener { fetchAllUsers() } // refresh list
            .addOnFailureListener { /* handle failure */ }
    }

    fun toggleUserStatus(userId: String) {
        val userRef = database.child(userId).child("isActive")
        userRef.get().addOnSuccessListener { snapshot ->
            val current = snapshot.getValue(Boolean::class.java) ?: true
            userRef.setValue(!current).addOnSuccessListener { fetchAllUsers() }
        }
    }

    fun updateUserPassword(userId: String, newPassword: String) {
        database.child(userId).child("password").setValue(newPassword)
            .addOnSuccessListener { fetchAllUsers() }
    }
}
