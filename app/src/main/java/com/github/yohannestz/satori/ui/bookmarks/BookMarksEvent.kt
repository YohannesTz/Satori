package com.github.yohannestz.satori.ui.bookmarks

import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.ui.base.event.PagedUiEvent

interface BookMarksEvent: PagedUiEvent {
    fun refreshList()
    fun onDeleteFromBookMarksClicked(item: BookMarkItem)
}