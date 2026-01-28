package com.example.smarthome.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smarthome.model.AdminModel
import com.example.smarthome.repo.AdminRepo
import com.example.smarthome.repo.AdminRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminViewModel(
    private val repo: AdminRepo = AdminRepoImpl()
) : ViewModel() {

    private val _users = MutableStateFlow<List<AdminModel>>(emptyList())
    val users = _users.asStateFlow()

    init { fetchAllUsers() }

    fun fetchAllUsers() {
        repo.getAllUsers { success, _, list ->
            if (success && list != null) _users.value = list
        }
    }

    fun addUser(email: String, password: String) {
        repo.addUser(email, password) { success, _ ->
            if (success) fetchAllUsers()
        }
    }

    fun updateUserStatus(userId: String, isActive: Boolean) {
        repo.updateUserStatus(userId, isActive) { success, _ ->
            if (success) fetchAllUsers()
        }
    }


    fun updateUserPassword(userId: String, newPassword: String, onComplete: ((Boolean, String) -> Unit)? = null) {
        repo.updateUserPassword(userId, newPassword) { success, message ->
            onComplete?.invoke(success, message)
        }
    }
}
