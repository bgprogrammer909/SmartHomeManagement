package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel

interface AdminRepo {
    fun addUser(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun getAllUsers(callback: (Boolean, String, List<AdminModel>?) -> Unit)
    fun updateUserStatus(userId: String, isActive: Boolean, callback: (Boolean, String) -> Unit)
    fun updateUserPassword(userId: String, newPassword: String, callback: (Boolean, String) -> Unit)
}
