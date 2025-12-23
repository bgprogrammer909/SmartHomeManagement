package com.example.smarthome.repo

import com.example.smarthome.model.ClimateModel

interface ClimateRepo {
    fun observeClimate(onChange: (ClimateModel) -> Unit)
    fun updateClimate(model: ClimateModel)
}
