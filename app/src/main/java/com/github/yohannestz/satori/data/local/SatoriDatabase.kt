package com.github.yohannestz.satori.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        SearchHistoryEntity::class,
    ],
    version = 1,
)
@TypeConverters(DatabaseConverters::class)
abstract class SatoriDatabase: RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}