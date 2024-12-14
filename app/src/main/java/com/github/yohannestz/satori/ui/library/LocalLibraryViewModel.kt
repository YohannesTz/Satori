package com.github.yohannestz.satori.ui.library

import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.data.repository.BookMarkRepository
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocalLibraryViewModel(
    private val bookMarkRepository: BookMarkRepository
) : BaseViewModel<LocalLibraryUiState>(), LocalLibraryEvent {
    override val mutableUiState: MutableStateFlow<LocalLibraryUiState> = MutableStateFlow(
        LocalLibraryUiState()
    )

    override fun onDeleteFromBookMarksClicked(item: BookMarkItem) {
        viewModelScope.launch {
            bookMarkRepository.deleteItemById(item.id)
        }
    }

    override fun refreshList() {
        mutableUiState.update { it.copy(nextPage = null, loadMore = true) }
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
        viewModelScope.launch {
            bookMarkRepository.getBookMarkedItems().collect { items ->
                mutableUiState.update {
                    it.copy(bookMarks = items, loadMore = false)
                }
            }
        }
    }
}