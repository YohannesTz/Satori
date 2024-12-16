package com.github.yohannestz.satori.di

import android.app.DownloadManager
import android.content.Context
import com.github.yohannestz.satori.data.downloader.FileDownloader
import com.github.yohannestz.satori.data.downloader.SatoriFileDownloader
import org.koin.dsl.module

val fileDownloaderModule = module {
    single {
        get<Context>().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    single<FileDownloader> {
        SatoriFileDownloader(
            get()
        )
    }
}