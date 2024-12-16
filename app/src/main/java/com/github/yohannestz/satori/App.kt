package com.github.yohannestz.satori

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.github.yohannestz.satori.di.dataStoreModule
import com.github.yohannestz.satori.di.databaseModule
import com.github.yohannestz.satori.di.fileDownloaderModule
import com.github.yohannestz.satori.di.networkModule
import com.github.yohannestz.satori.di.repositoryModule
import com.github.yohannestz.satori.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent, SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule,
                dataStoreModule,
                fileDownloaderModule
            )
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, percent = 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .crossfade(300)
            .build()
    }
}