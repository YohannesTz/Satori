package com.github.yohannestz.satori.ui.latest

import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.model.OrderBy
import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.data.model.volume.Item
import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import com.github.yohannestz.satori.utils.Extensions.addUniqueItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LatestViewModel(
    initialCategory: VolumeCategory? = null,
    private val bookRepository: BookRepository
) : BaseViewModel<LatestUiState>(), LatestEvent {

    override val mutableUiState: MutableStateFlow<LatestUiState> = MutableStateFlow(
        LatestUiState(categoryType = initialCategory)
    )

    override fun onItemSelected(item: Item) {
        mutableUiState.update { it.copy(selectedItem = item) }
    }

    override fun onCategorySelected(category: VolumeCategory) {
        viewModelScope.launch {
            mutableUiState.update {
                it.itemList.clear()
                it.copy(
                    categoryType = category,
                    nextPage = null,
                    loadMore = true,
                )
            }
        }
    }

    override fun refreshList() {
        mutableUiState.update { it.copy(nextPage = null, loadMore = true) }
    }

    override fun loadMore() {
        mutableUiState.value.run {
            if (canLoadMore && !isLoadingMore) {
                mutableUiState.update {
                    it.copy(loadMore = true)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            mutableUiState
                .distinctUntilChanged { old, new ->
                    old.loadMore == new.loadMore && old.categoryType == new.categoryType
                }
                .filter { it.categoryType != null && it.loadMore }
                .collectLatest { uiState ->
                    // Start loading
                    mutableUiState.update { state ->
                        state.copy(
                            isLoadingMore = true,
                            isLoading = uiState.nextPage == null
                        )
                    }

                    withContext(Dispatchers.IO) {
                        val result = bookRepository.getVolumesByCategory(
                            category = uiState.categoryType!!.value,
                            startIndex = uiState.nextPage ?: 0,
                            maxResults = 10,
                            orderBy = OrderBy.NEWEST.value
                        )

                        if (result.isSuccess) {
                            val newItems = result.getOrNull()?.items ?: emptyList()

                            if (uiState.nextPage == null) {
                                uiState.itemList.clear()
                            }

                            uiState.itemList.addUniqueItems(newItems)
                            val nextPage = if (newItems.isNotEmpty()) (uiState.nextPage ?: 0) + 10 else null

                            mutableUiState.update { state ->
                                state.copy(
                                    loadMore = false,
                                    nextPage = nextPage,
                                    isLoadingMore = false,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }
}