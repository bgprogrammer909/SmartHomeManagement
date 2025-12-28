package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminRepoImpl : AdminRepo {

    private val auth = FirebaseAuth.getInstance()
    private val ref = FirebaseDatabase.getInstance().getReference("users")

    override fun addUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        // Create user in Auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user!!.uid
                // Default modules
                val user = AdminModel(
                    id = uid,
                    email = email,
                    password = password,
                    isActive = true,
                    lights = false,
                    fan = false,
                    door = false
                )
                // Add to Realtime Database
                ref.child(uid).setValue(user)
                    .addOnSuccessListener { callback(true, "User added to Auth & DB") }
                    .addOnFailureListener { callback(false, it.message ?: "DB error") }
            }
            .addOnFailureListener { callback(false, it.message ?: "Auth error") }
    }

    override fun getAllUsers(callback: (Boolean, String, List<AdminModel>?) -> Unit) {
        ref.get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.children.mapNotNull { it.getValue(AdminModel::class.java) }
                callback(true, "Fetched", list)
            }
            .addOnFailureListener { callback(false, it.message ?: "Error", null) }
    }

    override fun updateUserStatus(userId: String, isActive: Boolean, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("isActive").setValue(isActive)
            .addOnSuccessListener { callback(true, "Status updated") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }

    override fun updateUserPassword(userId: String, newPassword: String, callback: (Boolean, String) -> Unit) {
        // Update password in Auth
        auth.fetchSignInMethodsForEmail(auth.currentUser?.email ?: "").addOnCompleteListener {
            val user = auth.currentUser
            user?.updatePassword(newPassword)
                ?.addOnSuccessListener {
                    // Update password in DB
                    ref.child(userId).child("password").setValue(newPassword)
                        .addOnSuccessListener { callback(true, "Password updated") }
                        .addOnFailureListener { callback(false, it.message ?: "DB error") }
                }
                ?.addOnFailureListener { callback(false, it.message ?: "Auth error") }
        }
    }

    override fun updateModule(userId: String, moduleName: String, moduleData: Any, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child(moduleName).setValue(moduleData)
            .addOnSuccessListener { callback(true, "$moduleName updated") }
            .addOnFailureListener { callback(false, it.message ?: "Error updating module") }
    }
}
