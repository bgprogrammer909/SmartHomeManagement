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
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid

                // Check if user is active
                ref.child(uid).child("isActive").get()
                    .addOnSuccessListener { snapshot ->
                        val isActive = snapshot.getValue(Boolean::class.java) ?: false
                        if (!isActive) {
                            auth.signOut()
                            callback(false, "Account disabled by admin")
                        } else {
                            callback(true, "Login successful")
                        }
                    }
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Login failed")
            }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { callback(true, "Reset link sent") }
            .addOnFailureListener { callback(false, it.message ?: "Failed") }
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun logOut(callback: (Boolean, String) -> Unit) {
        auth.signOut()
        callback(true, "Logged out")
    }

    override fun updatePassword(uid: String, newPassword: String, callback: (Boolean, String) -> Unit) {
        val user = auth.currentUser
        if (user != null && user.uid == uid) {
            user.updatePassword(newPassword)
                .addOnSuccessListener { callback(true, "Password updated") }
                .addOnFailureListener { callback(false, it.message ?: "Failed") }
        } else {
            callback(false, "Not authorized")
        }
    }
}
