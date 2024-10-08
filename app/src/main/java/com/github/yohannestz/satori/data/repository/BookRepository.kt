package com.github.yohannestz.satori.data.repository

import com.github.yohannestz.satori.data.remote.service.GoogleBooksApi

class BookRepository(
    private val googleBooksApi: GoogleBooksApi
) {
    suspend fun searchVolume(
        query: String,
        startIndex: Int,
        maxResults: Int,
        orderBy: String,
        printType: String?,
        filter: String?
    ) = googleBooksApi.searchVolume(
        query = query,
        startIndex = startIndex,
        maxResults = maxResults,
        orderBy = orderBy,
        printType = printType.takeIf { !it.isNullOrBlank() },
        filter = filter.takeIf { !it.isNullOrBlank() }
    )

    suspend fun getVolume(
        volumeId: String,
    ) = googleBooksApi.getVolumeById(
        id = volumeId
    )

    suspend fun getVolumesByCategory(
        category: String,
        startIndex: Int,
        maxResults: Int,
        orderBy: String,
        printType: String?,
        filter: String?
    ) = googleBooksApi.getVolumesByCategory(
        category = category,
        startIndex = startIndex,
        maxResults = maxResults,
        orderBy = orderBy,
        printType = printType.takeIf { !it.isNullOrBlank() },
        filter = filter.takeIf { !it.isNullOrBlank() }
    )
}
