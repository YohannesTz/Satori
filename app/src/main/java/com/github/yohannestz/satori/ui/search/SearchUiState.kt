package com.github.yohannestz.satori.ui.search

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.yohannestz.satori.data.model.volume.Item
import com.github.yohannestz.satori.data.model.volume.SearchHistory
import com.github.yohannestz.satori.ui.base.state.PagedUiState

@Stable
data class SearchUiState(
    val query: String = "",
    val searchHistoryList: List<SearchHistory> = emptyList(),
    val itemList: SnapshotStateList<Item> = mutableStateListOf(),
    val performSearch: Boolean = false,
    val noResult: Boolean = false,
    override val nextPage: Int? = null,
    override val loadMore: Boolean = false,
    override val isLoading: Boolean = false,
    override val message: String? = null
) : PagedUiState() {
    override fun setLoading(value: Boolean) = copy(isLoading = value)
    override fun setMessage(value: String?) = copy(message = value)
}