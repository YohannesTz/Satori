package com.github.yohannestz.satori.data.local.itembookmarks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item ORDER BY timestamp DESC LIMIT 10")
    fun getItemBookmarks(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM item LIMIT :pageSize OFFSET :startIndex")
    fun getPaginatedItemBookmarks(startIndex: Int, pageSize: Int): Flow<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemBookmark(entity: ItemEntity)

    @Delete
    suspend fun deleteItemBookmark(entity: ItemEntity)

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun deleteItemBookmarkById(id: String)

    @Query("SELECT COUNT(*) FROM item WHERE id = :itemId")
    suspend fun isItemBookmarked(itemId: String): Boolean
}