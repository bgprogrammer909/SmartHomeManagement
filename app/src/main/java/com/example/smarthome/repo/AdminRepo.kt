package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel

interface AdminRepo {
    fun addUserToDatabase(model: AdminModel, callback: (Boolean, String) -> Unit)
    fun getUserById(userId: String, callback: (Boolean, String, AdminModel?) -> Unit)
    fun getAllUsers(callback: (Boolean, String, List<AdminModel>?) -> Unit)
    fun updateUser(userId: String, model: AdminModel, callback: (Boolean, String) -> Unit)
    fun toggleUserStatus(userId: String, callback: (Boolean, String) -> Unit)
}