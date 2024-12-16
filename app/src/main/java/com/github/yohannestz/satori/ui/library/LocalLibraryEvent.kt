package com.github.yohannestz.satori.ui.library

import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.ui.base.event.PagedUiEvent

interface LocalLibraryEvent: PagedUiEvent {
    fun refreshList()
    fun checkPermissions()

    fun loadFiles(page: Int)
    fun onDeleteFromBookMarksClicked(item: BookMarkItem)
    fun onPermissionResult(granted: Boolean)
}