package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class LoginRepoImpl : LoginRepo {

    private val auth = FirebaseAuth.getInstance()
    private val ref = FirebaseDatabase.getInstance().getReference("users")

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { callback(true, "Login successful") }
            .addOnFailureListener { callback(false, it.message ?: "Login failed") }
    }

    fun registerUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        // Create in Auth first
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    val uid = user.uid
                    val adminModel = AdminModel(
                        id = uid,
                        email = email,
                        password = password,
                        isActive = true
                    )

                    // Add to Realtime Database
                    ref.child(uid).setValue(adminModel)
                        .addOnSuccessListener { callback(true, "User added to Auth & Database") }
                        .addOnFailureListener { callback(false, it.message ?: "DB write failed") }
                } else {
                    callback(false, "Failed to get UID")
                }
            }
            .addOnFailureListener { callback(false, it.message ?: "Registration failed") }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Reset link sent")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun logOut(callback: (Boolean, String) -> Unit) {
        auth.signOut()
        callback(true, "Logged out")
    }

    override fun updatePassword(uid: String, newPassword: String, callback: (Boolean, String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.uid == uid) {
            currentUser.updatePassword(newPassword)
                .addOnSuccessListener { callback(true, "Password updated") }
                .addOnFailureListener { callback(false, it.message ?: "Password update failed") }
        } else {
            // Admin changing another user's password cannot be done directly from app.
            callback(false, "Cannot update other user's password directly. Use Firebase Admin SDK on server.")
        }
    }
}
