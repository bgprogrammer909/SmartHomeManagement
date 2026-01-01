package com.example.smarthome.repo

import com.example.smarthome.model.ClimateModel

interface ClimateRepo {
    fun getFanRealtime(userId: String, callback: (success: Boolean, data: ClimateModel?) -> Unit)
    fun updateFan(userId: String, model: ClimateModel, callback: (success: Boolean, error: String?) -> Unit)
}
