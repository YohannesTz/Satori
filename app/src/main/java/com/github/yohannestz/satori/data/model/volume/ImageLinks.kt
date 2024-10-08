package com.github.yohannestz.satori.data.model.volume


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageLinks(
    @SerialName("smallThumbnail")
    val smallThumbnail: String,
    @SerialName("thumbnail")
    val thumbnail: String
)