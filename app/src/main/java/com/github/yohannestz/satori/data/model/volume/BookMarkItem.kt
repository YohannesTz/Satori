package com.github.yohannestz.satori.data.model.volume

data class BookMarkItem(
    val id: String,
    val etag: String,
    val title: String?,
    val authors: String?,
    val imageUrl: String?,
    val smallThumbnail: String?,
    val publisher: String?,
    val publishedDate: String?,
)