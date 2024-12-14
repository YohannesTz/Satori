package com.github.yohannestz.satori.ui.library

import androidx.compose.runtime.Stable
import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.ui.base.state.PagedUiState

@Stable
data class LocalLibraryUiState(
    val bookMarks: List<BookMarkItem> = emptyList(),
    val viewMode: ViewMode = ViewMode.LIST,
    val errorMessage: String? = null,
    val isLoadingMore: Boolean = false,
    val noResult: Boolean = false,
    override val nextPage: Int? = null,
    override val loadMore: Boolean = true,
    override val isLoading: Boolean = false,
    override val message: String? = null
) : PagedUiState() {
    override fun setLoading(value: Boolean) = copy(isLoading = value)
    override fun setMessage(value: String?) = copy(message = value)
}