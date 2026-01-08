package com.example.smarthome.repo

import com.example.smarthome.model.SecurityModel

interface SecurityRepo {
    fun observeSecurity(onChange: (SecurityModel) -> Unit)
    fun updateSecurity(model: SecurityModel)
}
