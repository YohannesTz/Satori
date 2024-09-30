package com.github.yohannestz.satori.ui.base

import com.github.yohannestz.satori.R

enum class StartTab(
    val value: String
) {
    LAST_USED("last_used"),
    HOME("home"),
    TOP("top"),
    BOOKMARKS("bookmarks"),
    MORE("more");

    val stringRes
        get() = when (this) {
            HOME -> R.string.title_home
            TOP -> R.string.title_top
            BOOKMARKS -> R.string.title_bookmarks
            MORE -> R.string.title_settings
            LAST_USED -> R.string.last_used
        }

    companion object {
        fun valueOf(tabName: String) = entries.find { it.value == tabName }
    }
}