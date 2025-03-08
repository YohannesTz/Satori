package com.github.yohannestz.satori.ui.settings

import com.github.yohannestz.satori.data.model.PrintType
import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.ui.base.StartTab
import com.github.yohannestz.satori.ui.base.ThemeStyle
import com.github.yohannestz.satori.ui.base.state.UiState

data class SettingsUiState(
    val theme: ThemeStyle = ThemeStyle.FOLLOW_SYSTEM,
    val useBlackColors: Boolean = false,
    val useDynamicColors: Boolean = false,
    val viewMode: ViewMode = ViewMode.LIST,
    val startTab: StartTab? = StartTab.LAST_USED,
    val onlyShowFreeContent: Boolean = false,
    val defaultPrintType: PrintType = PrintType.ALL,
    override val isLoading: Boolean = false,
    override val message: String? = null
) : UiState() {
    override fun setLoading(value: Boolean) = copy(isLoading = value)
    override fun setMessage(value: String?) = copy(message = value)
}