package com.example.smarthome.repo
import com.google.firebase.auth.FirebaseUser

interface LoginRepo {

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    )

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    )

    fun getCurrentUser(): FirebaseUser?

    fun logOut(
        callback: (Boolean, String) -> Unit
    )
}
