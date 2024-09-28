package com.github.yohannestz.satori.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessInfo(
    @SerialName("accessViewStatus")
    val accessViewStatus: String,
    @SerialName("country")
    val country: String,
    @SerialName("embeddable")
    val embeddable: Boolean,
    @SerialName("epub")
    val epub: Epub,
    @SerialName("pdf")
    val pdf: Pdf,
    @SerialName("publicDomain")
    val publicDomain: Boolean,
    @SerialName("quoteSharingAllowed")
    val quoteSharingAllowed: Boolean,
    @SerialName("textToSpeechPermission")
    val textToSpeechPermission: String,
    @SerialName("viewability")
    val viewability: String,
    @SerialName("webReaderLink")
    val webReaderLink: String
)