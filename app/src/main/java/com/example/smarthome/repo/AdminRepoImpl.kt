package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel
import com.google.firebase.database.*

class AdminRepoImpl : AdminRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("users")

    override fun addUserToDatabase(model: AdminModel, callback: (Boolean, String) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val existingIds = snapshot.children.mapNotNull {
                    it.child("id").getValue(String::class.java)?.toIntOrNull()
                }
                val nextId = if (existingIds.isEmpty()) 10000 else (existingIds.maxOrNull()!! + 1)
                val userWithId = AdminModel(
                    id = nextId.toString(),
                    password = model.password,
                    isActive = true
                )

                ref.child(userWithId.id).setValue(userWithId).addOnCompleteListener { task ->
                    if (task.isSuccessful) callback(true, "User added successfully")
                    else callback(false, task.exception?.message ?: "Error adding user")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message)
            }
        })
    }

    override fun getUserById(userId: String, callback: (Boolean, String, AdminModel?) -> Unit) {
        ref.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(AdminModel::class.java)
                if (user != null) callback(true, "User fetched", user)
                else callback(false, "User not found", null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getAllUsers(callback: (Boolean, String, List<AdminModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allUsers = snapshot.children.mapNotNull { it.getValue(AdminModel::class.java) }
                callback(true, "Users fetched", allUsers)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun updateUser(userId: String, model: AdminModel, callback: (Boolean, String) -> Unit) {
        ref.child(userId).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "User updated")
            else callback(false, it.exception?.message ?: "Error updating user")
        }
    }

    override fun toggleUserStatus(userId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("isActive").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentStatus = snapshot.getValue(Boolean::class.java) ?: true
                ref.child(userId).child("isActive").setValue(!currentStatus)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) callback(true, "Status toggled")
                        else callback(false, task.exception?.message ?: "Error toggling status")
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message)
            }
        })
    }
}
