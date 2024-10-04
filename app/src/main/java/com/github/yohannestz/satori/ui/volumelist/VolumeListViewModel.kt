package com.github.yohannestz.satori.ui.volumelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.yohannestz.satori.data.model.OrderBy
import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.data.repository.PreferencesRepository
import com.github.yohannestz.satori.ui.base.navigation.Route
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import com.github.yohannestz.satori.utils.Extensions.addUniqueItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.typeOf

class VolumeListViewModel(
    savedStateHandle: SavedStateHandle,
    private val defaultPreferencesRepository: PreferencesRepository,
    private val bookRepository: BookRepository
) : BaseViewModel<VolumeListUiState>(), VolumeListEvent {

    private val args = savedStateHandle.toRoute<Route.VolumeList>(
        typeMap = mapOf(typeOf<VolumeCategory>() to VolumeCategory.navType)
    )
    private val volumeCategory = args.volumeCategory

    override val mutableUiState: MutableStateFlow<VolumeListUiState> = MutableStateFlow(
        VolumeListUiState()
    )

    override fun onViewModeChanged(viewMode: ViewMode) {
        viewModelScope.launch {
            defaultPreferencesRepository.setVolumeListViewMode(viewMode)
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
        defaultPreferencesRepository.volumeListViewMode
            .distinctUntilChanged()
            .onEach { value ->
                mutableUiState.update {
                    it.copy(viewMode = value)
                }
            }
            .launchIn(viewModelScope)

        mutableUiState.update {
            it.copy(categoryType = volumeCategory)
        }

        viewModelScope.launch {
            mutableUiState
                .distinctUntilChanged { old, new ->
                    old.loadMore == new.loadMore && old.categoryType == new.categoryType
                }
                .filter { it.categoryType != null }
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
                            category = volumeCategory.value,
                            startIndex = uiState.nextPage ?: 0,
                            maxResults = 10,
                            orderBy = OrderBy.RELEVANCE.value
                        )

                        if (result.isSuccess) {
                            val newItems = result.getOrNull()?.items ?: emptyList()

                            // Clear items if it's the first page
                            if (uiState.nextPage == null) {
                                uiState.itemList.clear()
                            }

                            // Add unique items to the list
                            uiState.itemList.addUniqueItems(newItems)

                            // Determine the next page
                            val nextPage =
                                if (newItems.isNotEmpty()) (uiState.nextPage ?: 0) + 10 else null

                            // Update UI state after processing
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