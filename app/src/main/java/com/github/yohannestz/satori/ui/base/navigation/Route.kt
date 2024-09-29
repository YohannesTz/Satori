package com.github.yohannestz.satori.ui.base.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface Tab : Route {
        @Serializable
        data object Home : Tab

        @Serializable
        data object Top: Tab

        @Serializable
        data object Bookmarks: Tab

        @Serializable
        data object More: Tab
    }
}