package com.github.yohannestz.satori.data.downloader

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.github.yohannestz.satori.utils.SATORI_DOWNLOADS_DIR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class SatoriFileDownloader(
    private val downloadManager: DownloadManager
) : FileDownloader {
    override fun downloadFile(url: String, fileName: String?): Long {
        val downloadUri = Uri.parse(url)
        val targetFileName = fileName ?: downloadUri.lastPathSegment ?: "satori-book-download.pdf"
        val downloadsDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            SATORI_DOWNLOADS_DIR
        )
        if (!downloadsDirectory.exists()) {
            downloadsDirectory.mkdirs()
        }

        val fileDownloadFile = File(downloadsDirectory, targetFileName)

        //TODO make a settings to allow users to choose network
        val downloadRequest = DownloadManager.Request(downloadUri)
            .setDestinationUri(Uri.fromFile(fileDownloadFile))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

        return downloadManager.enqueue(downloadRequest)
    }

    override fun downloadFileWithProgress(
        url: String,
        fileName: String?
    ): Flow<FileDownloaderProgress> = flow {
        val downloadId = downloadFile(url, fileName)
        var isDownloading = true

        while (isDownloading) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor = downloadManager.query(query)

            if (cursor != null && cursor.moveToFirst()) {
                val bytesDownloadedIndex =
                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val bytesTotalIndex =
                    cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                if (bytesDownloadedIndex != -1 && bytesTotalIndex != -1) {
                    val bytesDownloaded =
                        cursor.getInt(bytesDownloadedIndex).toLong()
                    val bytesTotal =
                        cursor.getInt(bytesTotalIndex).toLong()

                    val progress = FileDownloaderProgress(
                        total = bytesTotal,
                        sent = bytesDownloaded,
                        status = if (bytesTotal < 0) FileDownloadStatus.PENDING else FileDownloadStatus.STARTED
                    )

                    emit(progress)
                }

                val status =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    val progress = FileDownloaderProgress(
                        total = 0,
                        sent = 0,
                        status = FileDownloadStatus.SUCCESSFUL
                    )
                    emit(progress)
                    isDownloading = false
                } else if (status == DownloadManager.STATUS_FAILED) {
                    val progress = FileDownloaderProgress(
                        total = 0,
                        sent = 0,
                        status = FileDownloadStatus.FAILED
                    )
                    emit(progress)
                    isDownloading = false
                }
            }
            cursor?.close()

            delay(1000L)
        }
    }.flowOn(Dispatchers.Default)
}