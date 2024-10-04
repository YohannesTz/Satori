package com.github.yohannestz.satori.ui.search

import com.github.yohannestz.satori.data.model.volume.SearchHistory
import com.github.yohannestz.satori.ui.base.event.PagedUiEvent

interface SearchEvent: PagedUiEvent {
    fun search(query: String)
    fun onSaveSearchHistory(query: String)
    fun onRemoveSearch(item: SearchHistory)
}