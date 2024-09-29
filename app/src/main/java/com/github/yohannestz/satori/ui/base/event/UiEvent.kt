package com.github.yohannestz.satori.ui.base.event

interface UiEvent {
    fun showMessage(message: String?)
    fun onMessageDisplayed()
}