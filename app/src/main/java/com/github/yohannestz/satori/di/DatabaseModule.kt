package com.github.yohannestz.satori.di

import android.content.Context
import androidx.room.Room
import com.github.yohannestz.satori.data.local.DatabaseMigrations
import com.github.yohannestz.satori.data.local.SatoriDatabase
import com.github.yohannestz.satori.data.local.SearchHistoryDao
import com.github.yohannestz.satori.data.repository.SearchHistoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    single<SatoriDatabase> { provideDatabase(androidContext()) }
    single<SearchHistoryDao> { get<SatoriDatabase>().searchHistoryDao() }
    singleOf(::SearchHistoryRepository)
}

private fun provideDatabase(context: Context): SatoriDatabase {
    return Room
        .databaseBuilder(
            context = context,
            klass = SatoriDatabase::class.java,
            name = "satori_database"
        )
        .addMigrations(*DatabaseMigrations.migrations)
        .build()
}