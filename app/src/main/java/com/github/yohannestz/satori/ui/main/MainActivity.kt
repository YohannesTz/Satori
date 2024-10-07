package com.github.yohannestz.satori.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.yohannestz.satori.ui.base.BottomDestination.Companion.isBottomDestination
import com.github.yohannestz.satori.ui.base.BottomDestination.Companion.isTopAppBarDisallowed
import com.github.yohannestz.satori.ui.base.BottomDestination.Companion.toBottomDestinationIndex
import com.github.yohannestz.satori.ui.base.ThemeStyle
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager.Companion.rememberNavActionManager
import com.github.yohannestz.satori.ui.main.composables.MainBottomNavBar
import com.github.yohannestz.satori.ui.main.composables.MainNavigationRail
import com.github.yohannestz.satori.ui.main.composables.MainTopAppBar
import com.github.yohannestz.satori.ui.theme.SatoriTheme
import com.github.yohannestz.satori.ui.theme.dark_scrim
import com.github.yohannestz.satori.ui.theme.light_scrim
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {

    val viewModel by viewModel<MainViewModel>()

    @OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val lastTabOpened = findLastTabOpened()
        val initialTheme = runBlocking { viewModel.theme.first() }
        val initialUseBlackColors = runBlocking { viewModel.useBlackColors.first() }

        setContent {
            KoinAndroidContext {
                val theme by viewModel.theme.collectAsStateWithLifecycle(initialValue = initialTheme)
                val useBlackColors by viewModel.useBlackColors.collectAsStateWithLifecycle(
                    initialValue = initialUseBlackColors
                )
                val isDark =
                    if (theme == ThemeStyle.FOLLOW_SYSTEM) isSystemInDarkTheme() else theme == ThemeStyle.DARK

                val navController = rememberNavController()
                val navActionManager = rememberNavActionManager(navController)
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val windowSizeClass = calculateWindowSizeClass(this)
                val isCompactScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

                SatoriTheme(
                    darkTheme = true,
                    useBlackColors = false
                ) {
                    val backgroundColor = MaterialTheme.colorScheme.background
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = backgroundColor
                    ) {
                        MainView(
                            isCompactScreen = isCompactScreen,
                            navController = navController,
                            navActionManager = navActionManager,
                            lastTabOpened = lastTabOpened,
                            saveLastTab = viewModel::saveLastTab
                        )

                        DisposableEffect(isDark, navBackStackEntry) {
                            var statusBarStyle = SystemBarStyle.auto(
                                android.graphics.Color.TRANSPARENT,
                                android.graphics.Color.TRANSPARENT
                            ) { isDark }

                            if (isCompactScreen && navBackStackEntry?.isBottomDestination() == true) {
                                statusBarStyle =
                                    if (isDark) SystemBarStyle.dark(backgroundColor.toArgb())
                                    else SystemBarStyle.light(
                                        backgroundColor.toArgb(),
                                        dark_scrim.toArgb()
                                    )
                            }

                            enableEdgeToEdge(
                                statusBarStyle = statusBarStyle,
                                navigationBarStyle = SystemBarStyle.auto(
                                    light_scrim.toArgb(),
                                    dark_scrim.toArgb(),
                                ) { isDark },
                            )
                            onDispose {}
                        }
                    }
                }
            }
        }
    }

    private fun findLastTabOpened(): Int {
        val startTab = runBlocking { viewModel.startTab.first() }
        var lastTabOpened =
            intent.action?.toBottomDestinationIndex() ?: startTab?.value?.toBottomDestinationIndex()

        if (lastTabOpened == null) {
            lastTabOpened = runBlocking { viewModel.lastTab.first() }
        } else {
            viewModel.saveLastTab(lastTabOpened)
        }
        return lastTabOpened
    }

}

@Composable
fun MainView(
    isCompactScreen: Boolean,
    navController: NavHostController,
    navActionManager: NavActionManager,
    lastTabOpened: Int,
    saveLastTab: (Int) -> Unit,
) {
    val density = LocalDensity.current

    val bottomBarState = remember { mutableStateOf(true) }
    var topBarHeightPx by remember { mutableFloatStateOf(0f) }
    val topBarOffsetY = remember { Animatable(0f) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isBottomDestination by remember {
        derivedStateOf { navBackStackEntry?.isBottomDestination() == true }
    }
    val isTopAppBarDisallowed by remember {
        derivedStateOf { navBackStackEntry?.isTopAppBarDisallowed() == true }
    }

    Scaffold(
        topBar = {
            if (isCompactScreen) {
                MainTopAppBar(
                    isVisible = (isBottomDestination && !isTopAppBarDisallowed),
                    navController = navController,
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = topBarOffsetY.value
                        }
                )
            }
        },
        bottomBar = {
            if (isCompactScreen) {
                MainBottomNavBar(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry,
                    isVisible = isBottomDestination,
                    onItemSelected = saveLastTab,
                    topBarOffsetY = topBarOffsetY
                )
            }
        }
    ) { padding ->
        if (!isCompactScreen) {
            MainNavigationRail(
                navController = navController,
                onItemSelected = saveLastTab,
                modifier = Modifier.padding(padding)
            )
            MainNavigation(
                navController = navController,
                navActionManager = navActionManager,
                lastTabOpened = lastTabOpened,
                isCompactScreen = false,
                modifier = Modifier,
                padding = padding,
                topBarHeightPx = topBarHeightPx,
                topBarOffsetY = topBarOffsetY
            )
        } else {
            LaunchedEffect(padding) {
                topBarHeightPx = with(density) { padding.calculateTopPadding().toPx() }
            }

            MainNavigation(
                navController = navController,
                navActionManager = navActionManager,
                lastTabOpened = lastTabOpened,
                isCompactScreen = true,
                modifier = Modifier.padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                ),
                padding = padding,
                topBarHeightPx = topBarHeightPx,
                topBarOffsetY = topBarOffsetY,
            )
        }
    }
}