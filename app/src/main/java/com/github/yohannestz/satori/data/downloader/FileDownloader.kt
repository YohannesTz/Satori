package com.github.yohannestz.satori.data.downloader

import com.github.yohannestz.satori.data.model.FileDownloaderProgress
import kotlinx.coroutines.flow.Flow

interface FileDownloader {
    fun downloadFile(url: String, fileName: String?): Long
    fun downloadFileWithProgress(url: String, fileName: String? = null): Flow<FileDownloaderProgress>
}