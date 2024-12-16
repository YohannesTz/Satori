package com.github.yohannestz.satori.di

import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.data.repository.FilesRepository
import com.github.yohannestz.satori.data.repository.GithubRepository
import com.github.yohannestz.satori.data.repository.PreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::BookRepository)
    singleOf(::GithubRepository)
    single { PreferencesRepository(get()) }
    single { FilesRepository(androidContext()) }
}