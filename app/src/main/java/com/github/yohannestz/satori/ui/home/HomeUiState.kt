package com.github.yohannestz.satori.ui.home

import androidx.compose.runtime.Immutable
import com.github.yohannestz.satori.data.model.volume.Item
import com.github.yohannestz.satori.ui.base.state.UiState

@Immutable
data class HomeUiState(
    val selfHelpBooks: List<Item> = emptyList(),
    val romanceBooks: List<Item> = emptyList(),
    val biographyBooks: List<Item> = emptyList(),
    val fictionBooks: List<Item> = emptyList(),
    override val isLoading: Boolean = true,
    override val message: String? = null
) : UiState() {
    override fun setLoading(value: Boolean) = copy(isLoading = value)
    override fun setMessage(value: String?) = copy(message = value)
}