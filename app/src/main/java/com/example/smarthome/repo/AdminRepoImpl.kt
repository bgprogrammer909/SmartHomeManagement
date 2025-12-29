package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminRepoImpl : AdminRepo {

    private val auth = FirebaseAuth.getInstance()
    private val ref = FirebaseDatabase.getInstance().getReference("users")

    override fun addUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user!!.uid

                val user = AdminModel(
                    id = uid,
                    email = email,
                    isActive = true,
                    lights = false,
                    fan = false,
                    door = false
                )

                ref.child(uid).setValue(user)
                    .addOnSuccessListener {
                        // VERY IMPORTANT
                        FirebaseAuth.getInstance().signOut()
                        callback(true, "User created successfully")
                    }
                    .addOnFailureListener {
                        callback(false, it.message ?: "Database error")
                    }
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Auth error")
            }
    }

    override fun getAllUsers(callback: (Boolean, String, List<AdminModel>?) -> Unit) {
        ref.get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.children.mapNotNull {
                    it.getValue(AdminModel::class.java)
                }
                callback(true, "Fetched", users)
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Error", null)
            }
    }

    override fun updateUserStatus(userId: String, isActive: Boolean, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("isActive").setValue(isActive)
            .addOnSuccessListener { callback(true, "Status updated") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }

    override fun updateModule(userId: String, moduleName: String, moduleData: Any, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child(moduleName).setValue(moduleData)
            .addOnSuccessListener { callback(true, "Module updated") }
            .addOnFailureListener { callback(false, it.message ?: "Update failed") }
    }
}
