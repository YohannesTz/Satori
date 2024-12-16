package com.github.yohannestz.satori.ui.details

import com.github.yohannestz.satori.data.model.volume.VolumeDetail
import com.github.yohannestz.satori.ui.base.event.UiEvent

interface VolumeDetailEvent : UiEvent {
    fun onDownloadPDFClicked(item: VolumeDetail?)
    fun onAddToBookMarkClicked(item: VolumeDetail?)
    fun onRemoveFromBookMarkClicked(item: VolumeDetail?)
}