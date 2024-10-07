package com.github.yohannestz.satori.ui.details

import androidx.compose.runtime.Immutable
import com.github.yohannestz.satori.data.model.volume.VolumeDetail
import com.github.yohannestz.satori.ui.base.state.UiState

@Immutable
data class VolumeDetailUiState(
    val volume: VolumeDetail? = null,
    val isBookMarked: Boolean = false,
    override val isLoading: Boolean = false,
    override val message: String? = null
) : UiState() {
    override fun setLoading(value: Boolean) = copy(isLoading = value)
    override fun setMessage(value: String?) = copy(message = value)
}