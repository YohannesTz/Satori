package com.github.yohannestz.satori.di

import com.github.yohannestz.satori.ui.main.MainViewModel
import com.github.yohannestz.satori.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
}