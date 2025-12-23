package com.example.smarthome.model

data class AdminModel(
    val id: String = "",
    val password: String = "",
    val isActive: Boolean = true
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "password" to password,
            "isActive" to isActive
        )
    }
}
