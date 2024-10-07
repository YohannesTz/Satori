package com.github.yohannestz.satori.data.repository

import com.github.yohannestz.satori.data.local.itembookmarks.ItemDao
import com.github.yohannestz.satori.data.local.itembookmarks.ItemEntity
import com.github.yohannestz.satori.data.local.itembookmarks.toBookMarkItemList
import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookMarkRepository(
    private val dao: ItemDao
) {
    fun getBookMarkedItems(): Flow<List<BookMarkItem>> {
        return dao.getItemBookmarks().map(List<ItemEntity>::toBookMarkItemList)
    }

    fun getPaginatedBookMarkedItems(startIndex: Int, pageSize: Int): Flow<List<BookMarkItem>> {
        return dao.getPaginatedItemBookmarks(startIndex, pageSize)
            .map(List<ItemEntity>::toBookMarkItemList)
    }

    suspend fun addItem(item: ItemEntity) {
        dao.insertItemBookmark(item)
    }

    suspend fun deleteItem(item: ItemEntity) {
        dao.deleteItemBookmark(item)
    }

    suspend fun deleteItemById(id: String) {
        dao.deleteItemBookmarkById(id)
    }

    suspend fun isItemBookmarked(itemId: String): Boolean {
        return dao.isItemBookmarked(itemId)
    }
}