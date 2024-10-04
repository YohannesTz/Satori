package com.github.yohannestz.satori.ui.latest

import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.data.model.volume.Item
import com.github.yohannestz.satori.ui.base.event.PagedUiEvent

interface LatestEvent : PagedUiEvent {
    fun refreshList()
    fun onItemSelected(item: Item)
    fun onCategorySelected(category: VolumeCategory)
}