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

    init {
        fetchAllUsers()
    }

    // Fetch all users from Firebase
    fun fetchAllUsers() {
        repo.getAllUsers { success, _, list ->
            if (success && list != null) {
                _users.value = list
            }
        }
    }

    // Add a new user
    fun addUser(email: String, password: String) {
        repo.addUser(email, password) { success, _ ->
            if (success) fetchAllUsers()
        }
    }

    // Update isActive status of a user
    fun updateUserStatus(userId: String, isActive: Boolean) {
        repo.updateUserStatus(userId, isActive) { success, _ ->
            if (success) fetchAllUsers()
        }
    }

    // Update any module of the user
    fun updateModule(userId: String, moduleName: String, moduleData: Any) {
        repo.updateModule(userId, moduleName, moduleData) { success, _ ->
            if (success) fetchAllUsers()
        }
    }

    // ===== NEW =====
    // Update user password (calls Repo -> Cloud Function)
    fun updateUserPassword(userId: String, newPassword: String, onComplete: ((Boolean, String) -> Unit)? = null) {
        repo.updateUserPassword(userId, newPassword) { success, message ->
            onComplete?.invoke(success, message)
        }
    }
}
