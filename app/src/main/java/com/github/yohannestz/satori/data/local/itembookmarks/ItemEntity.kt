package com.github.yohannestz.satori.data.local.itembookmarks

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "item",
    indices = [Index(value = ["id"], unique = true)],
)
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val etag: String,
    val authors: String?,
    val title: String?,
    val imageUrl: String?,
    val smallThumbnail: String?,
    val publisher: String?,
    val publishedDate: String?,
    val timestamp: Long = System.currentTimeMillis()
)
