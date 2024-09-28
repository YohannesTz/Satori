package com.github.yohannestz.satori.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Epub(
    @SerialName("downloadLink")
    val downloadLink: String,
    @SerialName("isAvailable")
    val isAvailable: Boolean
)