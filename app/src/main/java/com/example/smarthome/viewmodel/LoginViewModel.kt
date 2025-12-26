package com.example.smarthome.viewmodel
import androidx.lifecycle.ViewModel
import com.example.smarthome.repo.LoginRepo
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(val repo: LoginRepo) : ViewModel() {
    fun login(
        email: String, password: String, callback: (Boolean, String) -> Unit){
        repo.login(email, password, callback)
    }
    fun forgetPassword(
        email: String, callback: (Boolean, String) -> Unit){
        repo.forgetPassword(email, callback)
    }
    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }
    fun logOut(callback: (Boolean, String) -> Unit){
        repo.logOut(callback)
    }
}
