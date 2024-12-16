package com.github.yohannestz.satori.ui.library

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
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
    private val context: Context
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

    override fun checkPermissions() {
        val readPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val writePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        mutableUiState.update {
            it.copy(
                permissionsGranted = readPermission && writePermission
            )
        }
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
            val (files, hasMore) = filesRepository.getBookFilesFromDirectory(SATORI_DOWNLOADS_DIR, page, pageSize)
            mutableUiState.update {
                it.copy(
                    files = (it.files + files),
                    loadMore = hasMore,
                    nextPage = if (hasMore) page + 1 else null,
                    isLoadingMore = false,
                    noResult = files.isEmpty()
                )
            }

            currentPage = page
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

        checkPermissions()
    }
}