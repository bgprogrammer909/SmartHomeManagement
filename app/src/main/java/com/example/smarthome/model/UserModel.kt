package com.example.smarthome.model

data class UserModel(
    val userId:String = "",
    val userName:String = "",
    val password: String=""
){
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "userId" to userId,
            "userName" to userName,
            "password" to password
        )
    }
}


