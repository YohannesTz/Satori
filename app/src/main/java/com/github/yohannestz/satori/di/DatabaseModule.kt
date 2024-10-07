package com.github.yohannestz.satori.di

import android.content.Context
import androidx.room.Room
import com.github.yohannestz.satori.data.local.DatabaseMigrations
import com.github.yohannestz.satori.data.local.SatoriDatabase
import com.github.yohannestz.satori.data.local.itembookmarks.ItemDao
import com.github.yohannestz.satori.data.local.searchhistory.SearchHistoryDao
import com.github.yohannestz.satori.data.repository.SearchHistoryRepository
import com.github.yohannestz.satori.data.repository.BookMarkRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    single<SatoriDatabase> { provideDatabase(androidContext()) }
    single<SearchHistoryDao> { get<SatoriDatabase>().searchHistoryDao() }
    single<ItemDao> { get<SatoriDatabase>().itemDao() }

    singleOf(::SearchHistoryRepository)
    singleOf(::BookMarkRepository)
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