package com.example.smarthome.repo

interface EnergyRepo {
    fun getEnergyHistoryRealtime(
        userId: String,
        callback: (success: Boolean, history: Map<String, Float>?) -> Unit
    )
}






