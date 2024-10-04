package com.github.yohannestz.satori.di

import com.github.yohannestz.satori.ui.details.VolumeDetailViewModel
import com.github.yohannestz.satori.ui.home.HomeViewModel
import com.github.yohannestz.satori.ui.latest.LatestViewModel
import com.github.yohannestz.satori.ui.main.MainViewModel
import com.github.yohannestz.satori.ui.volumelist.VolumeListViewModel
import com.github.yohannestz.satori.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::LatestViewModel)
    viewModelOf(::VolumeDetailViewModel)
    viewModelOf(::VolumeListViewModel)
    viewModelOf(::SearchViewModel)
}