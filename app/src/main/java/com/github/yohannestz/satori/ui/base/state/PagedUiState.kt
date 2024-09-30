package com.github.yohannestz.satori.ui.base.state

abstract class PagedUiState : UiState() {
    abstract val nextPage: Int?
    abstract val loadMore: Boolean

    val canLoadMore get() = nextPage != null && !isLoading
}