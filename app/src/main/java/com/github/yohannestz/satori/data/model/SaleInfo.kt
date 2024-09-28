package com.github.yohannestz.satori.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaleInfo(
    @SerialName("buyLink")
    val buyLink: String,
    @SerialName("country")
    val country: String,
    @SerialName("isEbook")
    val isEbook: Boolean,
    @SerialName("saleability")
    val saleability: String
)