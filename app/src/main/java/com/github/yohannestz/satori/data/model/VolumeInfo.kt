package com.github.yohannestz.satori.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VolumeInfo(
    @SerialName("allowAnonLogging")
    val allowAnonLogging: Boolean,
    @SerialName("canonicalVolumeLink")
    val canonicalVolumeLink: String,
    @SerialName("categories")
    val categories: List<String>,
    @SerialName("contentVersion")
    val contentVersion: String,
    @SerialName("description")
    val description: String,
    @SerialName("imageLinks")
    val imageLinks: ImageLinks,
    @SerialName("industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>,
    @SerialName("infoLink")
    val infoLink: String,
    @SerialName("language")
    val language: String,
    @SerialName("maturityRating")
    val maturityRating: String,
    @SerialName("pageCount")
    val pageCount: Int,
    @SerialName("panelizationSummary")
    val panelizationSummary: PanelizationSummary,
    @SerialName("previewLink")
    val previewLink: String,
    @SerialName("printType")
    val printType: String,
    @SerialName("publishedDate")
    val publishedDate: String,
    @SerialName("readingModes")
    val readingModes: ReadingModes,
    @SerialName("title")
    val title: String,
    @SerialName("dimensions")
    val dimensions: Dimensions? = null,
)