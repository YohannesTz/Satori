package com.github.yohannestz.satori.ui.settings

import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.model.PrintType
import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.data.repository.PreferencesRepository
import com.github.yohannestz.satori.ui.base.StartTab
import com.github.yohannestz.satori.ui.base.ThemeStyle
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val defaultPreferenceRepository: PreferencesRepository
) : BaseViewModel<SettingsUiState>(), SettingsEvent {

    override val mutableUiState: MutableStateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState())

    override fun onThemeChanged(theme: ThemeStyle) {
        viewModelScope.launch {
            defaultPreferenceRepository.setTheme(theme)
        }
    }

    override fun onUseBlackColors(value: Boolean) {
        viewModelScope.launch {
            defaultPreferenceRepository.setUseBlackColors(value)
        }
    }

    override fun onViewModeChanged(value: ViewMode) {
        viewModelScope.launch {
            defaultPreferenceRepository.setVolumeListViewMode(value)
        }
    }

    override fun onStartTabChanged(value: StartTab?) {
        viewModelScope.launch {
            defaultPreferenceRepository.setStartTab(value)
        }
    }

    override fun onOnlyShowFreeContentChanged(value: Boolean) {
        viewModelScope.launch {
            defaultPreferenceRepository.setOnlyShowFreeContent(value)
        }
    }

    override fun onDefaultPrintTypeChanged(value: PrintType) {
        viewModelScope.launch {
            defaultPreferenceRepository.setDefaultPrintType(value)
        }
    }

    init {
        defaultPreferenceRepository.theme
            .onEach { value ->
                mutableUiState.update { it.copy(theme = value) }
            }
            .launchIn(viewModelScope)

        defaultPreferenceRepository.useBlackColors
            .onEach { value ->
                mutableUiState.update { it.copy(useBlackColors = value) }
            }
            .launchIn(viewModelScope)

        defaultPreferenceRepository.volumeListViewMode
            .onEach { value ->
                mutableUiState.update { it.copy(viewMode = value) }
            }
            .launchIn(viewModelScope)

        defaultPreferenceRepository.startTab
            .onEach { value ->
                value.let { mutableUiState.update { it.copy(startTab = value) } }
            }
            .launchIn(viewModelScope)

        defaultPreferenceRepository.onlyShowFreeContent
            .onEach { value ->
                mutableUiState.update { it.copy(onlyShowFreeContent = value) }
            }
            .launchIn(viewModelScope)

        defaultPreferenceRepository.defaultPrintType
            .onEach { value ->
                mutableUiState.update { it.copy(defaultPrintType = value) }
            }
            .launchIn(viewModelScope)
    }
}