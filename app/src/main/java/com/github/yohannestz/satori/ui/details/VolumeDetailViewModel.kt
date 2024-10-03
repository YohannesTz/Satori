package com.github.yohannestz.satori.ui.details

import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.repository.BookRepository
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VolumeDetailViewModel(
    volumeId: String,
    private val bookRepository: BookRepository
): BaseViewModel<VolumeDetailUiState>(), VolumeDetailEvent {
    override val mutableUiState: MutableStateFlow<VolumeDetailUiState> = MutableStateFlow(VolumeDetailUiState())

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
    }
}