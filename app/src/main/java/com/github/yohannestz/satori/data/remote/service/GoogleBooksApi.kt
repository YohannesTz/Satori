package com.github.yohannestz.satori.data.remote.service

import com.github.yohannestz.satori.data.model.volume.Volume
import com.github.yohannestz.satori.data.model.volume.VolumeDetail
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GoogleBooksApi(private val client: HttpClient) {

    suspend fun searchVolume(
        query: String,
        startIndex: Int,
        maxResults: Int,
        orderBy: String = "relevance"
    ): Result<Volume> {
        return try {
            val response: Volume = client.get("/books/v1/volumes") {
                parameter("q", query)
                parameter("orderBy", orderBy)
                parameter("startIndex", startIndex)
                parameter("maxResults", maxResults)
            }.body()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVolumeById(id: String): Result<VolumeDetail> {
        return try {
            val response: VolumeDetail = client.get("/books/v1/volumes/$id").body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVolumesByCategory(
        category: String,
        startIndex: Int,
        maxResults: Int,
        orderBy: String = "relevance"
    ): Result<Volume> {
        return try {
            val volumes: Volume = client.get("/books/v1/volumes") {
                parameter("q", "subject:$category")
                parameter("orderBy", orderBy)
                parameter("startIndex", startIndex)
                parameter("maxResults", maxResults)
            }.body()

            Result.success(volumes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}