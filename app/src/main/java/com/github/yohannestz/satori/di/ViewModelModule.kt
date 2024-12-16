package com.github.yohannestz.satori.di

import com.github.yohannestz.satori.ui.about.AboutViewModel
import com.github.yohannestz.satori.ui.details.VolumeDetailViewModel
import com.github.yohannestz.satori.ui.home.HomeViewModel
import com.github.yohannestz.satori.ui.latest.LatestViewModel
import com.github.yohannestz.satori.ui.library.LocalLibraryViewModel
import com.github.yohannestz.satori.ui.main.MainViewModel
import com.github.yohannestz.satori.ui.search.SearchViewModel
import com.github.yohannestz.satori.ui.settings.SettingsViewModel
import com.github.yohannestz.satori.ui.volumelist.VolumeListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::LatestViewModel)
    viewModelOf(::VolumeDetailViewModel)
    viewModelOf(::VolumeListViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::LocalLibraryViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::AboutViewModel)
}