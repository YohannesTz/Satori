package com.github.yohannestz.satori.data.remote.service

import com.github.yohannestz.satori.data.model.contributors.Contributor
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol

class GithubApi(private val client: HttpClient) {

    suspend fun getContributors(repoSlug: String): Result<List<Contributor>> {
        return try {
            val response: List<Contributor> = client.get("/repos/$repoSlug/contributors") {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.github.com"
                }
            }.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
