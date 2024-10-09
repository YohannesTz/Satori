package com.github.yohannestz.satori.ui.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.ui.about.AboutView
import com.github.yohannestz.satori.ui.base.BottomDestination
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.base.navigation.Route
import com.github.yohannestz.satori.ui.bookmarks.BookMarksView
import com.github.yohannestz.satori.ui.details.VolumeDetailView
import com.github.yohannestz.satori.ui.details.VolumeDetailViewModel
import com.github.yohannestz.satori.ui.home.HomeView
import com.github.yohannestz.satori.ui.latest.LatestView
import com.github.yohannestz.satori.ui.more.MoreView
import com.github.yohannestz.satori.ui.search.SearchHostView
import com.github.yohannestz.satori.ui.settings.SettingsView
import com.github.yohannestz.satori.ui.volumelist.VolumeListView
import com.github.yohannestz.satori.utils.MEDIA_DETAIL_ID
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

@Composable
fun MainNavigation(
    navController: NavHostController,
    navActionManager: NavActionManager,
    lastTabOpened: Int,
    isCompactScreen: Boolean,
    modifier: Modifier,
    padding: PaddingValues,
    topBarHeightPx: Float,
    topBarOffsetY: Animatable<Float, AnimationVector1D>,
) {
    NavHost(
        navController = navController,
        startDestination = BottomDestination.values
            .getOrElse(lastTabOpened) { BottomDestination.Home }.route,
        modifier = modifier,
        enterTransition = {
            fadeIn() + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        },
        exitTransition = {
            fadeOut() + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        },
        popEnterTransition = {
            fadeIn()
        },
        popExitTransition = {
            fadeOut() + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        }
    ) {
        composable<Route.Tab.Home>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() },
        ) {
            HomeView(
                navActionManager = navActionManager,
                padding = padding,
                topBarHeightPx = topBarHeightPx,
                topBarOffsetY = topBarOffsetY
            )
        }

        composable<Route.Tab.Latest>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() },
        ) {
            LatestView(
                navActionManager = navActionManager,
                isCompactScreen = isCompactScreen,
                padding = padding,
            )
        }

        composable<Route.Tab.Bookmarks>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() },
        ) {
            BookMarksView(
                navActionManager = navActionManager,
                isCompactScreen = isCompactScreen
            )
        }

        composable<Route.Tab.More>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() },
        ) {
            MoreView(
                navActionManager = navActionManager,
                padding = padding,
                topBarHeightPx = topBarHeightPx,
                topBarOffsetY = topBarOffsetY
            )
        }

        composable(
            route = "${Route.VolumeDetail.BASE_ROUTE}/{$MEDIA_DETAIL_ID}",
            arguments = listOf(navArgument(MEDIA_DETAIL_ID) { type = NavType.StringType }),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = Route.VolumeDetail.DEEPLINK_ROUTE
                }
            )
        ) { backStackEntry ->
            val volumeId = backStackEntry.arguments?.getString(MEDIA_DETAIL_ID) ?: ""
            val viewModel: VolumeDetailViewModel = koinViewModel {
                parametersOf(volumeId)
            }

            VolumeDetailView(
                navActionManager = navActionManager,
                viewModel = viewModel
            )
        }

        composable<Route.VolumeList>(
            typeMap = mapOf(typeOf<VolumeCategory>() to VolumeCategory.navType)
        ) {
            VolumeListView(
                navActionManager = navActionManager,
                isCompactScreen = isCompactScreen
            )
        }

        composable<Route.Search>(
            enterTransition = {
                expandVertically(expandFrom = Alignment.Top)
            },
            exitTransition = {
                shrinkVertically(shrinkTowards = Alignment.Top)
            },
            popEnterTransition = {
                expandVertically(expandFrom = Alignment.Top)
            },
            popExitTransition = {
                shrinkVertically(shrinkTowards = Alignment.Top)
            }
        ) {
            SearchHostView(
                isCompactScreen = isCompactScreen,
                padding = if (isCompactScreen) PaddingValues() else padding,
                navActionManager = navActionManager
            )
        }

        composable<Route.Settings> {
            SettingsView(
                navActionManager = navActionManager
            )
        }

        composable<Route.About> {
            AboutView(
                navActionManager = navActionManager
            )
        }
    }
}