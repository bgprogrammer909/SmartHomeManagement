// AdminViewModel.kt
package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smarthome.model.AdminModel
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminViewModel : ViewModel() {

    private val _users = MutableStateFlow<List<AdminModel>>(emptyList())
    val users: StateFlow<List<AdminModel>> = _users

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

    init {
        // Listen to all changes in Firebase in real-time
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(AdminModel::class.java) }
                _users.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Generate next unique ID from Firebase
    fun generateNextUserId(callback: (String) -> Unit) {
        database.get().addOnSuccessListener { snapshot ->
            val existingIds = snapshot.children.mapNotNull {
                it.child("id").getValue(String::class.java)?.toIntOrNull()
            }
            val nextId = if (existingIds.isEmpty()) 10000 else (existingIds.maxOrNull()!! + 1)
            callback(nextId.toString())
        }.addOnFailureListener {
            callback((10000..99999).random().toString())
        }
    }

    // Add new user, isActive true by default
    fun addUser(user: AdminModel) {
        val newUser = AdminModel(
            id = user.id,
            password = user.password,
            isActive = true
        )
        database.child(user.id).setValue(newUser)

    }

    // Update password
    fun updateUserPassword(id: String, newPassword: String) {
        database.child(id).child("password").setValue(newPassword)

    }

    // Toggle active/inactive
    fun toggleUserStatus(id: String) {
        val user = _users.value.find { it.id == id } ?: return
        val newStatus = !user.isActive
        database.child(id).child("isActive").setValue(newStatus)

    }
    fun fetchAllUsers() {
        // Just triggers the listener once; listener already updates _users
        database.get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull { it.getValue(AdminModel::class.java) }
            _users.value = list
        }
    }

}
