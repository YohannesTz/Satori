package com.github.yohannestz.satori.data.local

import com.github.yohannestz.satori.data.model.SearchHistory

fun SearchHistoryEntity.toSearchHistory(): SearchHistory {
    return SearchHistory(
        query = query,
        timestamp = timestamp
    )
}

fun SearchHistory.toSearchHistoryEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
        query = query,
        timestamp = timestamp
    )
}

fun List<SearchHistoryEntity>.toSearchHistoryList(): List<SearchHistory> {
    return map(SearchHistoryEntity::toSearchHistory)
}

fun List<SearchHistory>.toSearchHistoryEntityList(): List<SearchHistoryEntity> {
    return map(SearchHistory::toSearchHistoryEntity)
}