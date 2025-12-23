package com.example.smarthome.repo

interface UserRepo {
    fun login(
        userName: String,
        password: String,
        callback: (Boolean, String) -> Unit
    )
    fun register(
        userName: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    )

}