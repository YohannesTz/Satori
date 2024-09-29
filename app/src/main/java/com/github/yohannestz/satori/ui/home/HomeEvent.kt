package com.github.yohannestz.satori.ui.home

import com.github.yohannestz.satori.ui.base.event.UiEvent

interface HomeEvent : UiEvent {
    fun initRequestChain()
}

