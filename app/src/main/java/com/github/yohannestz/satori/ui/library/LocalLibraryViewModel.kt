package com.github.yohannestz.satori.ui.library

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.data.repository.BookMarkRepository
import com.github.yohannestz.satori.data.repository.FilesRepository
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import com.github.yohannestz.satori.utils.SATORI_DOWNLOADS_DIR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocalLibraryViewModel(
    private val bookMarkRepository: BookMarkRepository,
    private val filesRepository: FilesRepository,
) : BaseViewModel<LocalLibraryUiState>(), LocalLibraryEvent {

    private var currentPage = 0
    private val pageSize = 20

    override val mutableUiState: MutableStateFlow<LocalLibraryUiState> = MutableStateFlow(
        LocalLibraryUiState()
    )

    override fun onPermissionResult(granted: Boolean) {
        mutableUiState.update {
            it.copy(permissionsGranted = granted)
        }

        if (granted) {
            refreshList()
        }
    }

    override fun onDeleteFromBookMarksClicked(item: BookMarkItem) {
        viewModelScope.launch {
            bookMarkRepository.deleteItemById(item.id)
        }
    }

    override fun refreshList() {
        mutableUiState.update { it.copy(nextPage = null, loadMore = true) }
        loadFiles(0)
    }

    override fun loadMore() {
        mutableUiState.value.run {
            if (canLoadMore) {
                mutableUiState.update {
                    it.copy(loadMore = true)
                }
                loadFiles(currentPage + 1)
            }
        }
    }

    override fun loadFiles(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val (files, hasMore) = filesRepository.getBookFilesFromDirectory(
                    SATORI_DOWNLOADS_DIR, page, pageSize)
                Log.e("files", "files: ${files.size}")
                mutableUiState.update {
                    it.copy(
                        files = if (page == 0) files else (it.files + files),
                        loadMore = false,
                        nextPage = if (hasMore) page + 1 else null,
                        isLoadingMore = false,
                        isLoading = false,
                        noResult = files.isEmpty() && page == 0
                    )
                }
                currentPage = page
            } catch (e: Exception) {
                e.printStackTrace()
                mutableUiState.update {
                    it.copy(loadMore = false, isLoadingMore = false, isLoading = false)
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

        loadFiles(0)
    }
}
