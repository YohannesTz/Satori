package com.github.yohannestz.satori.data.repository

import com.github.yohannestz.satori.data.local.SearchHistoryDao
import com.github.yohannestz.satori.data.local.SearchHistoryEntity
import com.github.yohannestz.satori.data.local.toSearchHistoryEntity
import com.github.yohannestz.satori.data.local.toSearchHistoryList
import com.github.yohannestz.satori.data.model.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchHistoryRepository(
    private val dao: SearchHistoryDao
) {
    fun getSearchHistoryItems(): Flow<List<SearchHistory>> {
        return dao.getSearchHistory().map(List<SearchHistoryEntity>::toSearchHistoryList)
    }

    suspend fun addItem(query: String) {
        val trimmedQuery = query.trim()
    }

    suspend fun deleteItem(item: SearchHistory) {
        dao.deleteSearchHistory(item.toSearchHistoryEntity())
    }
}