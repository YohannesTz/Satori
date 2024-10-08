package com.github.yohannestz.satori.ui.about

import androidx.lifecycle.viewModelScope
import com.github.yohannestz.satori.data.repository.GithubRepository
import com.github.yohannestz.satori.ui.base.viewmodel.BaseViewModel
import com.github.yohannestz.satori.utils.GITHUB_SLUG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutViewModel(
    private val githubApiRepository: GithubRepository
) : BaseViewModel<AboutUiState>() {
    override val mutableUiState: MutableStateFlow<AboutUiState> = MutableStateFlow(AboutUiState())

    init {
        viewModelScope.launch {
            mutableUiState.update { it.copy(isLoading = true) }
            withContext(Dispatchers.IO) {
                val result = githubApiRepository.getContributors(GITHUB_SLUG)
                if (result.isSuccess) {
                    mutableUiState.update {
                        it.copy(
                            contributors = result.getOrNull() ?: emptyList(),
                            isLoading = false
                        )
                    }
                } else {
                    mutableUiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}