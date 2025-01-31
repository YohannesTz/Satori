package com.github.yohannestz.satori.data.model

import android.graphics.Bitmap
import android.net.Uri

data class PDFFile(
    val uri: Uri,
    val title: String,
    val author: String?,
    val thumbnail: Bitmap?,
    val fileSize: Long,
    val lastModified: Long
)