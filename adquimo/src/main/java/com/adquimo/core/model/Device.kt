package com.adquimo.core.model

data class Device (
    val timezoneId: String,
    val lang: String,
    val model: String,
    val os: String,
    val root: Boolean,
    val netType: String,
    val carrier: String
)