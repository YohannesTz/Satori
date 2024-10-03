package com.github.yohannestz.satori.ui.base.navigation

import com.github.yohannestz.satori.utils.VOLUME_DETAIL
import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface Tab : Route {
        @Serializable
        data object Home : Tab

        @Serializable
        data object Latest: Tab

        @Serializable
        data object Bookmarks: Tab

        @Serializable
        data object More: Tab
    }

    @Serializable
    data object VolumeList: Route

    @Serializable
    data class VolumeDetail(val volumeId: String): Route {
        companion object {
            const val BASE_ROUTE = VOLUME_DETAIL
            fun withArgs(volumeId: String) = "$BASE_ROUTE/$volumeId"
        }
    }
}