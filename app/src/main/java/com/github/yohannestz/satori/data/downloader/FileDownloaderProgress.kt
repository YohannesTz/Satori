package com.github.yohannestz.satori.data.downloader

enum class FileDownloadStatus {
    NOT_STARTED, PENDING, STARTED, FAILED, SUCCESSFUL
}

data class FileDownloaderProgress(
    val total: Long,
    val sent: Long,
    val status: FileDownloadStatus = FileDownloadStatus.NOT_STARTED
) {
    fun progressInFloat(): Float = if (total > 0) sent.toFloat() / total else 0f
    fun progressInPercentage(): Int = if (total > 0) ((sent * 100) / total).toInt() else 0
}