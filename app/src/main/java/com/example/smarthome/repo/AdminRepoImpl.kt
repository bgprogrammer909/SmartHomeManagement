package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel
import com.google.firebase.database.*

class AdminRepoImpl : AdminRepo {

    private val ref: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")

    override fun addUserToDatabase(
        model: AdminModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.get().addOnSuccessListener { snapshot ->

            val existingIds = snapshot.children.mapNotNull {
                it.key?.toIntOrNull()
            }

            val nextId = (existingIds.maxOrNull() ?: 9999) + 1

            val user = model.copy(
                id = nextId.toString(),
                isActive = true
            )

            ref.child(user.id).setValue(user)
                .addOnSuccessListener {
                    callback(true, "User added")
                }
                .addOnFailureListener {
                    callback(false, it.message ?: "Error")
                }
        }
    }

    override fun getAllUsers(
        callback: (Boolean, String, List<AdminModel>?) -> Unit
    ) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull {
                    it.getValue(AdminModel::class.java)
                }
                callback(true, "Fetched", users)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun updateUserStatus(
        userId: String,
        isActive: Boolean,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId)
            .child("isActive")
            .setValue(isActive)
            .addOnSuccessListener {
                callback(true, "Status updated")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Error")
            }
    }

    override fun updateUser(
        userId: String,
        model: AdminModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).setValue(model)
            .addOnSuccessListener {
                callback(true, "User updated")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Error")
            }
    }

    override fun getUserById(
        userId: String,
        callback: (Boolean, String, AdminModel?) -> Unit
    ) {
        ref.child(userId).get()
            .addOnSuccessListener {
                callback(true, "Fetched", it.getValue(AdminModel::class.java))
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Error", null)
            }
    }
}
