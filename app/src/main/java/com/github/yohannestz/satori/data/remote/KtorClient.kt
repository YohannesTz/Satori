package com.github.yohannestz.satori.data.remote

import com.github.yohannestz.satori.utils.GOOGLE_API_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val ktorHttpClient = HttpClient(OkHttp) {
    expectSuccess = false

    install(ContentNegotiation) {
        json(
            Json {
                coerceInputValues = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }

    install(HttpCache)

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
    }

    install(DefaultRequest) {
        url(GOOGLE_API_BASE_URL)
    }
}