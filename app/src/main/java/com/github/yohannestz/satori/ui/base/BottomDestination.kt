package com.github.yohannestz.satori.ui.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.ui.base.navigation.Route

sealed class BottomDestination(
    val value: String,
    val route: Any,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val iconSelected: Int
) {

    data object Home : BottomDestination(
        value = "home",
        route = Route.Tab.Home,
        title = R.string.title_home,
        icon = R.drawable.ic_outline_home_24,
        iconSelected = R.drawable.ic_round_home_24
    )

    data object Latest : BottomDestination(
        value = "latest",
        route = Route.Tab.Latest,
        title = R.string.title_latest,
        icon = R.drawable.ic_outline_fire_24,
        iconSelected = R.drawable.ic_round_fire_24
    )

    data object Bookmarks : BottomDestination(
        value = "bookmarks",
        route = Route.Tab.Bookmarks,
        title = R.string.title_bookmarks,
        icon = R.drawable.ic_outline_collections_bookmark_24,
        iconSelected = R.drawable.ic_round_collections_bookmark_24
    )

    data object More : BottomDestination(
        value = "settings",
        route = Route.Tab.More,
        title = R.string.title_settings,
        icon = R.drawable.ic_round_more_horiz_24,
        iconSelected = R.drawable.ic_round_more_horiz_24
    )

    companion object {
        val values = listOf(Home, Latest, Bookmarks, More)
        val railValues = listOf(Home, Latest, Bookmarks)
        private val topAppBarDisallowed = listOf(Bookmarks, More)

        fun String.toBottomDestinationIndex() = when (this) {
            Home.value -> 0
            Latest.value -> 1
            Bookmarks.value -> 2
            More.value -> 3
            else -> null
        }

        fun NavBackStackEntry.isBottomDestination() =
            destination.hierarchy.any { dest ->
                values.any { value -> dest.hasRoute(value.route::class) }
            }

        fun NavBackStackEntry.isTopAppBarDisallowed() =
            destination.hierarchy.any { dest ->
                topAppBarDisallowed.any { value -> dest.hasRoute(value.route::class) }
            }

        @Composable
        fun BottomDestination.Icon(selected: Boolean) {
            androidx.compose.material3.Icon(
                painter = painterResource(if (selected) iconSelected else icon),
                contentDescription = stringResource(title)
            )
        }
    }
}