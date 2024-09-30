package com.github.yohannestz.satori.data.model.volume


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("accessInfo")
    val accessInfo: AccessInfo,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("saleInfo")
    val saleInfo: SaleInfo,
    @SerialName("searchInfo")
    val searchInfo: SearchInfo? = null,
    @SerialName("selfLink")
    val selfLink: String,
    @SerialName("volumeInfo")
    val volumeInfo: VolumeInfo
)