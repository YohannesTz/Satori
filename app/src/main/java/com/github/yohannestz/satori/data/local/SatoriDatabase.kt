package com.github.yohannestz.satori.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.yohannestz.satori.data.local.itembookmarks.ItemDao
import com.github.yohannestz.satori.data.local.itembookmarks.ItemEntity
import com.github.yohannestz.satori.data.local.searchhistory.SearchHistoryDao
import com.github.yohannestz.satori.data.local.searchhistory.SearchHistoryEntity

@Database(
    entities = [
        SearchHistoryEntity::class,
        ItemEntity::class
    ],
    version = 3,
)
@TypeConverters(DatabaseConverters::class)
abstract class SatoriDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun itemDao(): ItemDao
}