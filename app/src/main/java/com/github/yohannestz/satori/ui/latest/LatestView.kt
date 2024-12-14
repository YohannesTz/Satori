package com.github.yohannestz.satori.ui.latest

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.ui.base.TabRowItem
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.base.navigation.Route
import com.github.yohannestz.satori.ui.composables.BaseListItemPlaceHolder
import com.github.yohannestz.satori.ui.composables.OnBottomReached
import com.github.yohannestz.satori.ui.composables.TabRowWithPager
import com.github.yohannestz.satori.ui.latest.composable.LatestListItem
import com.github.yohannestz.satori.utils.DEFAULT_GRID_SPAN_COUNT
import com.github.yohannestz.satori.utils.Extensions.collapsable
import com.github.yohannestz.satori.utils.Extensions.showToast
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun LatestView(
    isCompactScreen: Boolean,
    navActionManager: NavActionManager,
    nestedScrollConnection: NestedScrollConnection? = null,
    padding: PaddingValues
) {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val contentPadding = PaddingValues(
        bottom = padding.calculateBottomPadding() +
                systemBarsPadding.calculateBottomPadding()
    )

    LatestViewContent(
        isCompactScreen = isCompactScreen,
        navActionManager = navActionManager,
        nestedScrollConnection = nestedScrollConnection,
        contentPadding = contentPadding,
        padding = padding,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LatestViewContent(
    isCompactScreen: Boolean,
    navActionManager: NavActionManager,
    nestedScrollConnection: NestedScrollConnection? = null,
    contentPadding: PaddingValues,
    padding: PaddingValues
) {

    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val pullRefreshState = rememberPullToRefreshState()

    val tabRowItems = remember {
        VolumeCategory.entries.map {
            TabRowItem(value = it, title = it.label)
        }.toTypedArray()
    }

    val topBarHeightPx = 0f
    val topBarOffsetY: Animatable<Float, AnimationVector1D> = remember {
        Animatable(0f)
    }

    TabRowWithPager(
        tabs = tabRowItems,
        modifier = Modifier
            .padding(
                top = padding.calculateTopPadding()
            ),
        beyondBoundsPageCount = -1,
        isTabScrollable = true
    ) { pageIndex ->
        val selectedCategory = tabRowItems[pageIndex].value
        val event: LatestViewModel = koinViewModel(
            key = selectedCategory.value,
            parameters = { parametersOf(selectedCategory) }
        )
        val uiState by event.uiState.collectAsStateWithLifecycle()

        if (uiState.categoryType != selectedCategory) {
            event.onCategorySelected(selectedCategory)
        }

        LaunchedEffect(uiState.message) {
            if (uiState.message != null) {
                context.showToast(uiState.message!!)
                event.onMessageDisplayed()
            }
        }

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { event.refreshList() },
            state = pullRefreshState
        ) {
            val listModifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .then(
                    if (nestedScrollConnection != null)
                        Modifier.nestedScroll(nestedScrollConnection)
                    else Modifier
                )

            if (isCompactScreen) {
                val listState = rememberLazyListState()
                listState.OnBottomReached(buffer = 3) {
                    event.loadMore()
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
                        start = contentPadding.calculateStartPadding(layoutDirection) + 8.dp,
                        top = contentPadding.calculateTopPadding() + 8.dp,
                        end = contentPadding.calculateEndPadding(layoutDirection) + 8.dp,
                        bottom = contentPadding.calculateBottomPadding() + 8.dp
                    ),
                ) {
                    items(
                        items = uiState.itemList,
                        key = { item -> "${item.id}-${item.etag}" },
                        contentType = { it.volumeInfo }
                    ) { item ->
                        LatestListItem(
                            item = item,
                            onClick = {
                                navActionManager.navigateToDetail(item.id)
                            }
                        )

                        if (uiState.isLoadingMore || uiState.itemList.size < 5) {
                            this@LazyColumn.items(5, contentType = { it }) {
                                BaseListItemPlaceHolder()
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(DEFAULT_GRID_SPAN_COUNT),
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
                        LatestListItem(
                            item = item,
                            onClick = {
                                navActionManager.navigateTo(Route.VolumeDetail(""))
                            }
                        )

                        if (uiState.isLoadingMore) {
                            this@LazyVerticalGrid.items(5, contentType = { it }) {
                                BaseListItemPlaceHolder()
                            }
                        }
                    }

                    item(contentType = { 0 }) {
                        if (uiState.canLoadMore) {
                            Box(modifier = Modifier.align(Alignment.Center)) {
                                BaseListItemPlaceHolder()
                            }
                            LaunchedEffect(true) {
                                event.loadMore()
                            }
                        }
                    }
                }
            }
        }
    }
}