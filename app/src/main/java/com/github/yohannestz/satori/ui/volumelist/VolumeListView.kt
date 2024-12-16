package com.github.yohannestz.satori.ui.volumelist

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.data.model.ViewMode
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.composables.BackIconButton
import com.github.yohannestz.satori.ui.composables.BaseListItemPlaceHolder
import com.github.yohannestz.satori.ui.composables.DefaultScaffoldWithMediumTopAppBar
import com.github.yohannestz.satori.ui.composables.OnBottomReached
import com.github.yohannestz.satori.ui.volumelist.composables.VolumeListGridItem
import com.github.yohannestz.satori.ui.volumelist.composables.VolumeListGridItemPlaceHolder
import com.github.yohannestz.satori.ui.volumelist.composables.VolumeListItem
import com.github.yohannestz.satori.utils.DEFAULT_GRID_SPAN_COUNT
import com.github.yohannestz.satori.utils.Extensions.collapsable
import com.github.yohannestz.satori.utils.Extensions.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun VolumeListView(
    navActionManager: NavActionManager,
    isCompactScreen: Boolean,
    nestedScrollConnection: NestedScrollConnection? = null,
    topBarHeightPx: Float = 0f,
    topBarOffsetY: Animatable<Float, AnimationVector1D> = Animatable(0f),
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: VolumeListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VolumeListContent(
        uiState = uiState,
        event = viewModel,
        navActionManager = navActionManager,
        isCompactScreen = isCompactScreen,
        nestedScrollConnection = nestedScrollConnection,
        topBarHeightPx = topBarHeightPx,
        topBarOffsetY = topBarOffsetY,
        contentPadding = contentPadding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VolumeListContent(
    uiState: VolumeListUiState,
    event: VolumeListEvent?,
    navActionManager: NavActionManager,
    isCompactScreen: Boolean,
    nestedScrollConnection: NestedScrollConnection? = null,
    topBarHeightPx: Float = 0f,
    topBarOffsetY: Animatable<Float, AnimationVector1D> = Animatable(0f),
    contentPadding: PaddingValues = PaddingValues(),
) {
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val pullToRefreshState = rememberPullToRefreshState()

    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            context.showToast(uiState.message)
        }
    }

    DefaultScaffoldWithMediumTopAppBar(
        title = if (uiState.categoryType?.label != null) stringResource(uiState.categoryType.label) else stringResource(
            R.string.app_name
        ),
        navigationIcon = {
            BackIconButton(onClick = navActionManager::goBack)
        },
        actions = {
            IconButton(
                onClick = {
                    val value =
                        if (uiState.viewMode == ViewMode.GRID) ViewMode.LIST else ViewMode.GRID
                    event?.onViewModeChanged(value)
                }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (uiState.viewMode == ViewMode.GRID) R.drawable.ic_round_grid_view_24
                        else R.drawable.ic_round_view_list_24
                    ),
                    contentDescription = stringResource(R.string.view_mode)
                )
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
        contentWindowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal)
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { event?.refreshList() },
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            ) {
            val listModifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .then(
                    if (nestedScrollConnection != null)
                        Modifier.nestedScroll(nestedScrollConnection)
                    else Modifier
                )

            if (uiState.viewMode == ViewMode.GRID) {
                val listState = rememberLazyGridState()
                LazyVerticalGrid(
                    columns = if (isCompactScreen) GridCells.Fixed(DEFAULT_GRID_SPAN_COUNT) else GridCells.Adaptive(
                        150.dp
                    ),
                    modifier = listModifier
                        .collapsable(
                            state = listState,
                            topBarHeightPx = topBarHeightPx,
                            topBarOffsetY = topBarOffsetY,
                        ),
                    state = listState,
                    contentPadding = PaddingValues(
                        start = contentPadding.calculateStartPadding(layoutDirection) + 8.dp,
                        top = contentPadding.calculateTopPadding() + 8.dp,
                        end = contentPadding.calculateEndPadding(layoutDirection) + 8.dp,
                        bottom = contentPadding.calculateBottomPadding() + 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    items(
                        items = uiState.itemList,
                        key = { item -> "${item.id}-${item.etag}" },
                        contentType = { it.volumeInfo }
                    ) { item ->
                        VolumeListGridItem(
                            item = item,
                            onClick = {
                                navActionManager.navigateToDetail(item.id)
                            }
                        )

                    }

                    if (uiState.isLoadingMore) {
                        items(8, contentType = { it }) {
                            VolumeListGridItemPlaceHolder()
                        }
                    }

                    item(contentType = { 0 }) {
                        if (uiState.canLoadMore) {
                            Box(modifier = Modifier.align(Alignment.Center)) {
                                VolumeListGridItemPlaceHolder()
                            }
                            LaunchedEffect(true) {
                                event?.loadMore()
                            }
                        }
                    }
                }
            } else {
                val listState = rememberLazyListState()
                listState.OnBottomReached(buffer = 3) {
                    event?.loadMore()
                }
                LazyColumn(
                    modifier = listModifier
                        .collapsable(
                            state = listState,
                            topBarHeightPx = topBarHeightPx,
                            topBarOffsetY = topBarOffsetY,
                        ),
                    state = listState,
                    contentPadding = PaddingValues(
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        top = contentPadding.calculateTopPadding() + 8.dp,
                        end = contentPadding.calculateEndPadding(layoutDirection),
                        bottom = contentPadding.calculateBottomPadding() + 8.dp
                    ),
                ) {
                    items(
                        items = uiState.itemList,
                        key = { item -> "${item.id}-${item.etag}" },
                        contentType = { it.volumeInfo }
                    ) { item ->
                        VolumeListItem(
                            item = item,
                            onClick = {
                                navActionManager.navigateToDetail(item.id)
                            }
                        )
                    }

                    if (uiState.isLoadingMore) {
                        items(8, contentType = { it }) {
                            BaseListItemPlaceHolder()
                        }
                    }
                }
            }
        }
    }
}