package com.github.yohannestz.satori.ui.base.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.github.yohannestz.satori.ui.base.event.UiEvent
import com.github.yohannestz.satori.ui.base.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


abstract class BaseAndroidViewModel<S : UiState>(
    context: Context
) : AndroidViewModel(context as Application), UiEvent {

    protected abstract val mutableUiState: MutableStateFlow<S>
    val uiState: StateFlow<S> by lazy { mutableUiState.asStateFlow() }

    @Suppress("UNCHECKED_CAST")
    fun setLoading(value: Boolean) {
        mutableUiState.update { it.setLoading(value) as S }
    }

    @Suppress("UNCHECKED_CAST")
    override fun showMessage(message: String?) {
        mutableUiState.update { it.setMessage(message ?: GENERIC_ERROR) as S }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onMessageDisplayed() {
        mutableUiState.update { it.setMessage(null) as S }
    }

    companion object {
        private const val GENERIC_ERROR = "Generic Error"
        const val FLOW_TIMEOUT = 5_000L
    }
}