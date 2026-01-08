package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.functions.FirebaseFunctions

class AdminRepoImpl : AdminRepo {

    private val auth = FirebaseAuth.getInstance()
    private val ref = FirebaseDatabase.getInstance().getReference("users")

    // ====== ADD USER ======
    override fun addUser(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener

                val user = AdminModel(
                    id = uid,
                    email = email,
                    isActive = true
                )

                ref.child(uid).setValue(user)
                    .addOnSuccessListener {
                        auth.signOut() // keep admin logged out of new user
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

    // ====== FETCH ALL USERS ======
    override fun getAllUsers(
        callback: (Boolean, String, List<AdminModel>?) -> Unit
    ) {
        ref.get().addOnSuccessListener { snapshot ->
            val users = mutableListOf<AdminModel>()
            for (userSnap in snapshot.children) {
                val id = userSnap.key ?: continue
                val email = userSnap.child("email").getValue(String::class.java) ?: ""
                val isActive = try {
                    val raw = userSnap.child("isActive").value
                    when (raw) {
                        is Boolean -> raw
                        is String -> raw.toBoolean()
                        else -> false
                    }
                } catch (e: Exception) { false }

                users.add(AdminModel(id, email, isActive))
            }
            callback(true, "Fetched users", users)
        }.addOnFailureListener {
            callback(false, it.message ?: "Error fetching users", null)
        }
    }

    // ====== UPDATE USER STATUS ======
    override fun updateUserStatus(
        userId: String,
        isActive: Boolean,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).child("isActive")
            .setValue(isActive)
            .addOnSuccessListener { callback(true, "Status updated") }
            .addOnFailureListener { callback(false, it.message ?: "Status update failed") }
    }



    // ====== UPDATE USER PASSWORD via Cloud Function ======
    override fun updateUserPassword(
        userId: String,
        newPassword: String,
        callback: (Boolean, String) -> Unit
    ) {
        val functions = FirebaseFunctions.getInstance()
        val data = hashMapOf(
            "userId" to userId,
            "newPassword" to newPassword
        )

        functions.getHttpsCallable("updateUserPassword")
            .call(data)
            .addOnSuccessListener { callback(true, "Password updated") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to update password") }
    }
}
