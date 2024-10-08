package com.github.yohannestz.satori.ui.about

import com.github.yohannestz.satori.data.model.contributors.Contributor
import com.github.yohannestz.satori.ui.base.state.UiState

data class AboutUiState(
    val contributors: List<Contributor> = emptyList(),
    override val isLoading: Boolean = false,
    override val message: String? = null
) : UiState() {
    override fun setLoading(value: Boolean) = copy(isLoading = value)
    override fun setMessage(value: String?) = copy(message = value)
}