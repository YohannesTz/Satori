package com.github.yohannestz.satori.ui.home

import com.github.yohannestz.satori.data.model.OrderBy
import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel(
    private val bookRepository: BookRepository
) : BaseViewModel<HomeUiState>(), HomeEvent {

    override val mutableUiState = MutableStateFlow(HomeUiState())

    override fun initRequestChain() {

    }

    private suspend fun getSelfHelpBooks() {
        val result = bookRepository.getVolumesByCategory(
            category = VolumeCategory.SELF_HELP.value,
            startIndex = 0,
            maxResults = 20,
            orderBy = OrderBy.RELEVANCE.value
        )

        if (result.isSuccess) {
            mutableUiState.value = mutableUiState.value.copy(
                selfHelpBooks = result.getOrNull()?.items ?: emptyList()
            )
        } else {
            showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
        }
    }

    private suspend fun getRomanceBooks() {
        val result = bookRepository.getVolumesByCategory(
            category = VolumeCategory.ROMANCE.value,
            startIndex = 0,
            maxResults = 20,
            orderBy = OrderBy.RELEVANCE.value
        )

        if (result.isSuccess) {
            mutableUiState.value = mutableUiState.value.copy(
                selfHelpBooks = result.getOrNull()?.items ?: emptyList()
            )
        } else {
            showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
        }
    }

    private suspend fun getBiographyBooks() {
        val result = bookRepository.getVolumesByCategory(
            category = VolumeCategory.BIOGRAPHY.value,
            startIndex = 0,
            maxResults = 20,
            orderBy = OrderBy.RELEVANCE.value
        )

        if (result.isSuccess) {
            mutableUiState.value = mutableUiState.value.copy(
                selfHelpBooks = result.getOrNull()?.items ?: emptyList()
            )
        } else {
            showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
        }
    }

    private suspend fun getFictionBooks() {
        val result = bookRepository.getVolumesByCategory(
            category = VolumeCategory.FICTION.value,
            startIndex = 0,
            maxResults = 20,
            orderBy = OrderBy.RELEVANCE.value
        )

        if (result.isSuccess) {
            mutableUiState.value = mutableUiState.value.copy(
                selfHelpBooks = result.getOrNull()?.items ?: emptyList()
            )
        } else {
            showMessage(result.exceptionOrNull()?.message ?: "Something went wrong")
        }
    }
}