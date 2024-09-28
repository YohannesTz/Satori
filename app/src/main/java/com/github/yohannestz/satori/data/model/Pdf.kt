package com.github.yohannestz.satori.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pdf(
    @SerialName("isAvailable")
    val isAvailable: Boolean
)