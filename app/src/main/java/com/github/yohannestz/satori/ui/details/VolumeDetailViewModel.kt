package com.github.yohannestz.satori.ui.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.downloader.FileDownloader
import com.github.yohannestz.satori.data.local.itembookmarks.toItemEntity
import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.data.model.volume.VolumeDetail
import com.github.yohannestz.satori.data.repository.BookMarkRepository
import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import com.github.yohannestz.satori.utils.Extensions.treatForFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VolumeDetailViewModel(
    volumeId: String,
    private val bookMarkRepository: BookMarkRepository,
    private val bookRepository: BookRepository,
    private val fileDownloader: FileDownloader
) : BaseViewModel<VolumeDetailUiState>(), VolumeDetailEvent {
    override val mutableUiState: MutableStateFlow<VolumeDetailUiState> =
        MutableStateFlow(VolumeDetailUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            val result = bookRepository.getVolume(volumeId)

            if (result.isSuccess) {
                mutableUiState.value = mutableUiState.value.copy(
                    volume = result.getOrNull(),
                    isLoading = false
                )
            } else {
                showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
                setLoading(false)
            }
        }

        viewModelScope.launch {
            val isBookMarked = bookMarkRepository.isItemBookmarked(volumeId)

            mutableUiState.update {
                it.copy(isBookMarked = isBookMarked)
            }
        }
    }

    override fun onDownloadPDFClicked(item: VolumeDetail?) {
        if (item == null) {
            showMessage("Item was not found")
            return
        }

        val downloadUrl = item.accessInfo.pdf.acsTokenLink
        if (downloadUrl.isNullOrEmpty()) {
            showMessage("Item is not available")
            return
        }

        val secureDownloadUrl = if(downloadUrl.startsWith("http:", ignoreCase = true)) {
            downloadUrl.replaceFirst("http:", "https:")
        } else {
            downloadUrl
        }

        viewModelScope.launch {
            fileDownloader.downloadFileWithProgress(secureDownloadUrl, "${item.volumeInfo.title?.treatForFileName()}.pdf")
                .collect { downloadProgress ->
                    mutableUiState.update {
                        it.copy(progress = downloadProgress)
                    }
                }
        }
    }

    override fun onAddToBookMarkClicked(item: VolumeDetail?) {
        if (item != null) {
            viewModelScope.launch {
                val bookMarkItem = BookMarkItem(
                    id = item.id,
                    etag = item.etag,
                    title = item.volumeInfo.title,
                    authors = item.volumeInfo.authors?.joinToString(),
                    imageUrl = item.volumeInfo.imageLinks?.thumbnail,
                    smallThumbnail = item.volumeInfo.imageLinks?.smallThumbnail,
                    publisher = item.volumeInfo.publisher,
                    publishedDate = item.volumeInfo.publishedDate
                )
                bookMarkRepository.addItem(bookMarkItem.toItemEntity())
                mutableUiState.update {
                    it.copy(isBookMarked = true)
                }
            }
        }
    }

    override fun onRemoveFromBookMarkClicked(item: VolumeDetail?) {
        if (item != null) {
            viewModelScope.launch {
                bookMarkRepository.deleteItemById(item.id)
                mutableUiState.update {
                    it.copy(isBookMarked = false)
                }
            }
        }
    }
}