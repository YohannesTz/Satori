package com.github.yohannestz.satori.di

import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.data.repository.PreferencesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::BookRepository)
    single { PreferencesRepository(get()) }
}