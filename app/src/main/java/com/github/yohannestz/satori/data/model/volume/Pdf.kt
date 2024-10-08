package com.github.yohannestz.satori.data.model.volume


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pdf(
    @SerialName("isAvailable")
    val isAvailable: Boolean,

    @SerialName("acsTokenLink")
    val acsTokenLink: String? = null
)