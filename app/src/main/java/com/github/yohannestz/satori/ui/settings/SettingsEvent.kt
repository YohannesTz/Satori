package com.github.yohannestz.satori.ui.settings

import com.github.yohannestz.satori.data.model.PrintType
import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.ui.base.StartTab
import com.github.yohannestz.satori.ui.base.ThemeStyle
import com.github.yohannestz.satori.ui.base.event.UiEvent

interface SettingsEvent : UiEvent {
    fun onThemeChanged(theme: ThemeStyle)
    fun onUseBlackColors(value: Boolean)
    fun onUseDynamicColors(value: Boolean)
    fun onViewModeChanged(value: ViewMode)
    fun onStartTabChanged(value: StartTab?)
    fun onOnlyShowFreeContentChanged(value: Boolean)
    fun onDefaultPrintTypeChanged(value: PrintType)
}