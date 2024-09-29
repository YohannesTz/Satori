package com.github.yohannestz.satori.data.model.volume


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadingModes(
    @SerialName("image")
    val image: Boolean,
    @SerialName("text")
    val text: Boolean
)