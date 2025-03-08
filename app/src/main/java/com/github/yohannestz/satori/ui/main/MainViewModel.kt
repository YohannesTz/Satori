package com.github.yohannestz.satori.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.repository.PreferencesRepository
import com.github.yohannestz.satori.ui.base.ThemeStyle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val startTab = preferencesRepository.startTab
    val lastTab = preferencesRepository.lastTab

    fun saveLastTab(value: Int) = viewModelScope.launch {
        preferencesRepository.setLastTab(value)
    }

    val theme = preferencesRepository.theme
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeStyle.FOLLOW_SYSTEM)

    val useBlackColors = preferencesRepository.useBlackColors
    val useDynamicColors = preferencesRepository.useDynamicColors
}