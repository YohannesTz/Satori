package com.github.yohannestz.satori.ui.home

import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.model.OrderBy
import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val bookRepository: BookRepository
) : BaseViewModel<HomeUiState>(), HomeEvent {

    override val mutableUiState = MutableStateFlow(HomeUiState())

    override fun initRequestChain() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.value.run {
                setLoading(true)
                if (selfHelpBooks.isEmpty()) getSelfHelpBooks()
                if (historyBooks.isEmpty()) getHistoryBooks()
                if (biographyBooks.isEmpty()) getBiographyBooks()
                if (fictionBooks.isEmpty()) getFictionBooks()
                setLoading(false)
            }
        }
    }

    private suspend fun getSelfHelpBooks() {
        val result = bookRepository.getVolumesByCategory(
            category = VolumeCategory.SELF_HELP.value,
            startIndex = 0,
            maxResults = 10,
            orderBy = OrderBy.RELEVANCE.value
        )

        if (result.isSuccess) {
            mutableUiState.value = mutableUiState.value.copy(
                selfHelpBooks = result.getOrNull()?.items ?: emptyList()
            )
        } else {
            showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
            setLoading(false)
        }
    }

    private suspend fun getHistoryBooks() {
        val result = bookRepository.getVolumesByCategory(
            category = VolumeCategory.HISTORY.value,
            startIndex = 0,
            maxResults = 10,
            orderBy = OrderBy.RELEVANCE.value
        )

        if (result.isSuccess) {
            mutableUiState.value = mutableUiState.value.copy(
                historyBooks = result.getOrNull()?.items ?: emptyList()
            )
        } else {
            showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
        }
    }

    private suspend fun getBiographyBooks() {
        val result = bookRepository.getVolumesByCategory(
            category = VolumeCategory.BIOGRAPHY.value,
            startIndex = 0,
            maxResults = 10,
            orderBy = OrderBy.RELEVANCE.value
        )

        if (result.isSuccess) {
            mutableUiState.value = mutableUiState.value.copy(
                biographyBooks = result.getOrNull()?.items ?: emptyList()
            )
        } else {
            showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
        }
    }

    private suspend fun getFictionBooks() {
        val result = bookRepository.getVolumesByCategory(
            category = VolumeCategory.FICTION.value,
            startIndex = 0,
            maxResults = 10,
            orderBy = OrderBy.RELEVANCE.value
        )

        if (result.isSuccess) {
            mutableUiState.value = mutableUiState.value.copy(
                fictionBooks = result.getOrNull()?.items ?: emptyList()
            )
        } else {
            showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
        }
    }
}