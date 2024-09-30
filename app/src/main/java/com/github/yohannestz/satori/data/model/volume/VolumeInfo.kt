package com.github.yohannestz.satori.data.model.volume

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object AverageRatingSerializer : KSerializer<Float> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("FloatOrInt", PrimitiveKind.FLOAT)

    override fun deserialize(decoder: Decoder): Float {
        return when (val value = decoder.decodeDouble()) {
            value.toInt().toDouble() -> value.toInt().toFloat()
            else -> value.toFloat()
        }
    }

    override fun serialize(encoder: Encoder, value: Float) {
        encoder.encodeFloat(value)
    }

}

@Serializable
data class VolumeInfo(
    @SerialName("allowAnonLogging")
    val allowAnonLogging: Boolean,
    @SerialName("canonicalVolumeLink")
    val canonicalVolumeLink: String,
    @SerialName("categories")
    val categories: List<String>? = null,
    @SerialName("contentVersion")
    val contentVersion: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("imageLinks")
    val imageLinks: ImageLinks? = null,
    @SerialName("industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>? = null,
    @SerialName("infoLink")
    val infoLink: String,
    @SerialName("language")
    val language: String,
    @SerialName("maturityRating")
    val maturityRating: String,
    @SerialName("pageCount")
    val pageCount: Int? = null,
    @SerialName("panelizationSummary")
    val panelizationSummary: PanelizationSummary? = null,
    @SerialName("previewLink")
    val previewLink: String,
    @SerialName("printType")
    val printType: String,
    @SerialName("publishedDate")
    val publishedDate: String? = null,
    @SerialName("readingModes")
    val readingModes: ReadingModes,
    @SerialName("title")
    val title: String? = null,
    @SerialName("dimensions")
    val dimensions: Dimensions? = null,
    @SerialName("authors")
    val authors: List<String>? = null,
    @SerialName("publisher")
    val publisher: String? = null,
    @Serializable(with = AverageRatingSerializer::class)
    @SerialName("averageRating")
    val averageRating: Float? = null,
)