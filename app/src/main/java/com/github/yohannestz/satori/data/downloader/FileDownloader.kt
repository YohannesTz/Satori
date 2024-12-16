package com.github.yohannestz.satori.data.downloader

import kotlinx.coroutines.flow.Flow

interface FileDownloader {
    fun downloadFile(url: String, fileName: String?): Long
    fun downloadFileWithProgress(url: String, fileName: String? = null): Flow<FileDownloaderProgress>
}