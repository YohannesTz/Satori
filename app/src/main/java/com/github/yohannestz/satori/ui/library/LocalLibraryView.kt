package com.github.yohannestz.satori.ui.library

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.data.model.LocalLibrary
import com.github.yohannestz.satori.ui.base.TabRowItem
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.composables.BaseListItemPlaceHolder
import com.github.yohannestz.satori.ui.composables.DefaultTopAppBar
import com.github.yohannestz.satori.ui.composables.OnBottomReached
import com.github.yohannestz.satori.ui.composables.TabRowWithPager
import com.github.yohannestz.satori.ui.library.composables.BookMarksGridItem
import com.github.yohannestz.satori.ui.library.composables.BookMarksListItem
import com.github.yohannestz.satori.utils.DEFAULT_GRID_SPAN_COUNT
import com.github.yohannestz.satori.utils.Extensions.collapsable
import com.github.yohannestz.satori.utils.Extensions.showToast
import org.koin.androidx.compose.koinViewModel

private fun checkStoragePermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.isExternalStorageManager()
    } else {
        val readPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val writePermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        readPermission && writePermission
    }
}

@Composable
fun LocalLibraryView(
    navActionManager: NavActionManager,
    isCompactScreen: Boolean,
    padding: PaddingValues,
    topBarHeightPx: Float = 0f,
    topBarOffsetY: Animatable<Float, AnimationVector1D> = Animatable(0f),
) {
    val viewModel: LocalLibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocalLibraryViewContent(
        uiState = uiState,
        event = viewModel,
        navActionManager = navActionManager,
        isCompactScreen = isCompactScreen,
        topBarHeightPx = topBarHeightPx,
        topBarOffsetY = topBarOffsetY,
        padding = padding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalLibraryViewContent(
    uiState: LocalLibraryUiState,
    event: LocalLibraryEvent?,
    navActionManager: NavActionManager,
    isCompactScreen: Boolean,
    topBarHeightPx: Float = 0f,
    topBarOffsetY: Animatable<Float, AnimationVector1D> = Animatable(0f),
    padding: PaddingValues
) {
    val context = LocalContext.current
    val pullToRefreshState = rememberPullToRefreshState()
    val localLibraryTabRowItems = remember {
        LocalLibrary.entries.map {
            TabRowItem(value = it, title = it.label)
        }.toTypedArray()
    }

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            context.showToast(uiState.message)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        val allPermissionsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val write = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
        event?.onPermissionResult(allPermissionsGranted)
    }

    LaunchedEffect(Unit) {
        val hasPermission = checkStoragePermissions(context)

        if (!hasPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    permissionLauncher.launch(intent)
                } catch (e: Exception) {
                    val fallbackIntent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    permissionLauncher.launch(fallbackIntent)
                }
            } else {
                permissionLauncher.launch(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                )
            }
        } else {
            event?.loadFiles(0)
        }
    }

    Column {
        DefaultTopAppBar(
            title = stringResource(R.string.local_library),
            showNavigateBack = false,
            navigateBack = {}
        )

        TabRowWithPager(
            tabs = localLibraryTabRowItems,
            beyondBoundsPageCount = -1,
            isTabScrollable = true
        ) { pageIndex ->
            val selectedLocalLibraryItem = localLibraryTabRowItems[pageIndex].value
            if (selectedLocalLibraryItem == LocalLibrary.BOOKMARKS) {
                PullToRefreshBox(
                    isRefreshing = uiState.isLoading || uiState.isLoadingMore,
                    onRefresh = { event?.refreshList() },
                    state = pullToRefreshState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val listModifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                    val listState = rememberLazyListState()
                    listState.OnBottomReached(buffer = 3) {
                        event?.loadMore()
                    }

                    if (isCompactScreen) {
                        LazyColumn(
                            modifier = listModifier
                                .collapsable(
                                    state = listState,
                                    topBarHeightPx = topBarHeightPx,
                                    topBarOffsetY = topBarOffsetY,
                                ),
                            state = listState
                        ) {
                            items(
                                items = uiState.bookMarks,
                                key = { item -> item.id },
                                contentType = { it }
                            ) { item ->
                                BookMarksListItem(
                                    modifier = Modifier.animateItem(),
                                    item = item,
                                    onClick = { navActionManager.navigateToDetail(item.id) }
                                )
                            }

                            if (uiState.isLoadingMore) {
                                items(8, contentType = { it }) {
                                    BaseListItemPlaceHolder()
                                }
                            }
                        }
                    } else {
                        val gridState = rememberLazyGridState()
                        gridState.OnBottomReached(buffer = 3) {
                            event?.loadMore()
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(DEFAULT_GRID_SPAN_COUNT),
                            modifier = Modifier.fillMaxSize(),
                            state = gridState
                        ) {
                            items(
                                items = uiState.bookMarks,
                                key = { item -> item.id },
                                contentType = { it }
                            ) {
                                BookMarksGridItem(
                                    item = it,
                                    onClick = { navActionManager.navigateToDetail(it.id) },
                                    onRemoveIconClicked = { event?.onDeleteFromBookMarksClicked(it) }
                                )
                            }
                        }
                    }
                }
            } else {
                PullToRefreshBox(
                    isRefreshing = uiState.isLoading || uiState.isLoadingMore,
                    onRefresh = { event?.refreshList() },
                    state = pullToRefreshState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val listModifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                    val listState = rememberLazyListState()
                    listState.OnBottomReached(buffer = 3) {
                        event?.loadMore()
                    }

                    if (isCompactScreen) {
                        LazyColumn(
                            modifier = listModifier
                                .collapsable(
                                    state = listState,
                                    topBarHeightPx = topBarHeightPx,
                                    topBarOffsetY = topBarOffsetY,
                                ),
                            state = listState
                        ) {
                            items(
                                items = uiState.files,
                                contentType = { it }
                            ) { item ->
                                Text(
                                    text = item.name,
                                )
                            }

                            if (uiState.isLoadingMore) {
                                items(8, contentType = { it }) {
                                    BaseListItemPlaceHolder()
                                }
                            }
                        }
                    } else {
                        val gridState = rememberLazyGridState()
                        gridState.OnBottomReached(buffer = 3) {
                            event?.loadMore()
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(DEFAULT_GRID_SPAN_COUNT),
                            modifier = Modifier.fillMaxSize(),
                            state = gridState
                        ) {
                            items(
                                items = uiState.files,
                                contentType = { it }
                            ) {
                                Text(
                                    text = it.name
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}