package com.github.yohannestz.satori.ui.latest

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.data.model.volume.Item
import com.github.yohannestz.satori.ui.base.state.PagedUiState

@Stable
data class LatestUiState(
    val categoryType: VolumeCategory?,
    val itemList: SnapshotStateList<Item> = mutableStateListOf(),
    val isLoadingMore: Boolean = false,
    val selectedItem: Item? = null,
    val totalItems: Int = 0,
    val noResult: Boolean = false,
    override val nextPage: Int? = null,
    override val loadMore: Boolean = true,
    override val isLoading: Boolean = true,
    override val message: String? = null
): PagedUiState() {
    override fun setLoading(value: Boolean) = copy(isLoading = value)
    override fun setMessage(value: String?) = copy(message = value)
}