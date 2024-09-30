package com.github.yohannestz.satori

import android.app.Application
import com.github.yohannestz.satori.di.dataStoreModule
import com.github.yohannestz.satori.di.databaseModule
import com.github.yohannestz.satori.di.networkModule
import com.github.yohannestz.satori.di.repositoryModule
import com.github.yohannestz.satori.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule,
                dataStoreModule
            )
        }
    }
}