package com.github.yohannestz.satori.di

import com.github.yohannestz.satori.data.remote.ktorHttpClient
import com.github.yohannestz.satori.data.remote.service.GithubApi
import com.github.yohannestz.satori.data.remote.service.GoogleBooksApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    single { ktorHttpClient }
    singleOf(::GoogleBooksApi)
    singleOf(::GithubApi)
}