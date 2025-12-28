package com.example.smarthome.repo

import com.example.smarthome.model.EnergyModel

interface EnergyRepo {
    fun observeEnergy(onChange: (EnergyModel) -> Unit)
    fun updateEnergy(model: EnergyModel)
}