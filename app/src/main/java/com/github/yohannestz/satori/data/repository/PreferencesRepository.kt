package com.github.yohannestz.satori.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.yohannestz.satori.data.model.PrintType
import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.di.getValue
import com.github.yohannestz.satori.di.setValue
import com.github.yohannestz.satori.ui.base.StartTab
import com.github.yohannestz.satori.ui.base.ThemeStyle
import kotlinx.coroutines.flow.map

class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    val theme = dataStore.getValue(THEME_KEY, ThemeStyle.FOLLOW_SYSTEM.name)
        .map { ThemeStyle.valueOfOrNull(it) ?: ThemeStyle.LIGHT }

    suspend fun setTheme(value: ThemeStyle) {
        dataStore.setValue(THEME_KEY, value.name)
    }

    val useBlackColors = dataStore.getValue(USE_BLACK_COLORS_KEY, true)
    suspend fun setUseBlackColors(value: Boolean) {
        dataStore.setValue(USE_BLACK_COLORS_KEY, value)
    }

    val useDynamicColors = dataStore.getValue(USE_DYNAMIC_COLORS_KEY, false)
    suspend fun setUseDynamicColors(value: Boolean) {
        dataStore.setValue(USE_DYNAMIC_COLORS_KEY, value)
    }

    val startTab = dataStore.getValue(START_TAB_KEY, StartTab.LAST_USED.value)
        .map { StartTab.valueOf(tabName = it) }

    suspend fun setStartTab(value: StartTab?) {
        dataStore.setValue(START_TAB_KEY, value!!.value)
    }

    val lastTab = dataStore.getValue(LAST_TAB_KEY, 0)
    suspend fun setLastTab(value: Int) {
        dataStore.setValue(LAST_TAB_KEY, value)
    }

    val volumeListViewMode = dataStore.getValue(VOLUME_LIST_VIEW_MODE, ViewMode.LIST.name)
        .map { ViewMode.valueOfOrNull(it) ?: ViewMode.LIST }

    suspend fun setVolumeListViewMode(value: ViewMode) {
        dataStore.setValue(VOLUME_LIST_VIEW_MODE, value.name)
    }

    val onlyShowFreeContent = dataStore.getValue(ONLY_SHOW_FREE_CONTENT, false)
    suspend fun setOnlyShowFreeContent(value: Boolean) {
        dataStore.setValue(ONLY_SHOW_FREE_CONTENT, value)
    }

    val defaultPrintType = dataStore.getValue(DEFAULT_PRINT_TYPE, PrintType.ALL.value)
        .map { PrintType.valueOfOrNull(it) ?: PrintType.ALL }

    suspend fun setDefaultPrintType(value: PrintType) {
        dataStore.setValue(DEFAULT_PRINT_TYPE, value.value)
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val USE_BLACK_COLORS_KEY = booleanPreferencesKey("use_black_colors")
        private val USE_DYNAMIC_COLORS_KEY = booleanPreferencesKey("use_dynamic_colors")
        private val LAST_TAB_KEY = intPreferencesKey("last_tab")
        private val START_TAB_KEY = stringPreferencesKey("start_tab")
        private val VOLUME_LIST_VIEW_MODE = stringPreferencesKey("volume_list_view_mode")
        private val ONLY_SHOW_FREE_CONTENT = booleanPreferencesKey("only_show_free_content")
        private val DEFAULT_PRINT_TYPE = stringPreferencesKey("default_print_type")
    }
}