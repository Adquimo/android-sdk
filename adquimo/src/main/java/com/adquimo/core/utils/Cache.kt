package com.adquimo.core.utils

import android.annotation.SuppressLint
import android.content.Context
import com.adquimo.core.api.Client
import com.adquimo.core.api.Service
import com.adquimo.core.repository.ConfigRepository

object Cache {
    @SuppressLint("StaticFieldLeak")
    lateinit var configRepository: ConfigRepository
    lateinit var restClient: Service

    var token: String? = null

    fun initialize(context: Context) {
        configRepository = ConfigRepository(context)
        restClient = Client.getInstance()
    }
}