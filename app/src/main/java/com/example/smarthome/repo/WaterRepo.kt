package com.example.smarthome.repo

import com.example.smarthome.model.WaterModel

interface WaterRepo {
    fun observeWater(onChange: (WaterModel) -> Unit)
    fun updateWater(model: WaterModel)
}