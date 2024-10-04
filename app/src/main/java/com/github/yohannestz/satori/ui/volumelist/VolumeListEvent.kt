package com.github.yohannestz.satori.ui.volumelist

import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.ui.base.event.PagedUiEvent

interface VolumeListEvent: PagedUiEvent {
    fun onViewModeChanged(viewMode: ViewMode)
    fun refreshList()
}