package com.github.yohannestz.satori.ui.library

import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.ui.base.event.PagedUiEvent

interface LocalLibraryEvent: PagedUiEvent {
    fun refreshList()
    fun onDeleteFromBookMarksClicked(item: BookMarkItem)
}