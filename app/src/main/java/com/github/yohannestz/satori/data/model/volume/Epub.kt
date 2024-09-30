package com.github.yohannestz.satori.data.model.volume


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Epub(
    @SerialName("downloadLink")
    val downloadLink: String? = null,
    @SerialName("isAvailable")
    val isAvailable: Boolean
)