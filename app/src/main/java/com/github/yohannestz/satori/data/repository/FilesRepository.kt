package com.github.yohannestz.satori.data.repository

import android.content.Context
import android.os.Environment
import java.io.File

class FilesRepository(
    private val context: Context
) {
    fun getBookFilesFromDirectory(
        directoryName: String,
        page: Int,
        pageSize: Int
    ): Pair<List<File>, Boolean> {
        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), directoryName)

        if (!directory.exists()) {
            return Pair(emptyList(), false)
        }

        val allFiles = directory.listFiles { _, name ->
            name.endsWith(".pdf", true) || name.endsWith(".epub", true)
        }?.toList().orEmpty()

        val pagedFiles = allFiles
            .sortedByDescending { it.lastModified() }
            .drop(page * pageSize)
            .take(pageSize)

        val hasMore = allFiles.size > (page + 1) * pageSize
        return Pair(pagedFiles, hasMore)
    }
}