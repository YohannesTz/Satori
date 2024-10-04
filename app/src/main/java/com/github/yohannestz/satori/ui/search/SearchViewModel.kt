package com.github.yohannestz.satori.ui.search

import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.model.OrderBy
import com.github.yohannestz.satori.data.model.volume.SearchHistory
import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.data.repository.SearchHistoryRepository
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

class SearchViewModel(
    private val bookRepository: BookRepository,
    private val searchHistoryRepository: SearchHistoryRepository
): BaseViewModel<SearchUiState>(), SearchEvent {
    override val mutableUiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState())

    override fun search(query: String) {
        mutableUiState.update {
            it.copy(
                query = query,
                performSearch = true,
                nextPage = null
            )
        }
        onSaveSearchHistory(query)
    }

    override fun onSaveSearchHistory(query: String) {
        viewModelScope.launch {
            searchHistoryRepository.addItem(query)
        }
    }

    override fun onRemoveSearch(item: SearchHistory) {
        viewModelScope.launch {
            searchHistoryRepository.deleteItemByQuery(item.query)
        }
    }

    override fun loadMore() {
        mutableUiState.value.run {
            if (canLoadMore) {
                mutableUiState.update {
                    it.copy(loadMore = true)
                }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState
                .distinctUntilChanged {old, new ->
                    old.performSearch == new.performSearch
                            && old.query == new.query
                }
                .filter { it.query.isNotBlank() && (it.performSearch || it.loadMore) }
                .collectLatest { uiState ->
                    setLoading(uiState.nextPage == null)

                    withContext(Dispatchers.IO) {
                        val result = bookRepository.searchVolume(
                            query = uiState.query,
                            startIndex = uiState.nextPage ?: 0,
                            maxResults = 10,
                            orderBy = OrderBy.RELEVANCE.value
                        )

                        if (result.isSuccess) {
                            if (uiState.performSearch) uiState.itemList.clear()
                            val newItems = result.getOrNull()?.items ?: emptyList()

                            uiState.itemList.addUniqueItems(newItems)

                            val nextPage = if (newItems.isNotEmpty()) (uiState.nextPage ?: 0) + 10 else null
                            mutableUiState.update { state ->
                                state.copy(
                                    loadMore = false,
                                    nextPage = nextPage,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }

        viewModelScope.launch {
            searchHistoryRepository.getSearchHistoryItems().collect { searchHistoryList ->
                mutableUiState.update {
                    it.copy(searchHistoryList = searchHistoryList)
                }
            }
        }
    }
}