package com.github.yohannestz.satori.ui.bookmarks

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.bookmarks.composables.BookMarksGridItem
import com.github.yohannestz.satori.ui.bookmarks.composables.BookMarksListItem
import com.github.yohannestz.satori.ui.composables.BackIconButton
import com.github.yohannestz.satori.ui.composables.BaseListItemPlaceHolder
import com.github.yohannestz.satori.ui.composables.DefaultScaffoldWithMediumTopAppBar
import com.github.yohannestz.satori.ui.composables.OnBottomReached
import com.github.yohannestz.satori.utils.DEFAULT_GRID_SPAN_COUNT
import com.github.yohannestz.satori.utils.Extensions.collapsable
import com.github.yohannestz.satori.utils.Extensions.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookMarksView(
    navActionManager: NavActionManager,
    isCompactScreen: Boolean,
    nestedScrollConnection: NestedScrollConnection? = null,
    topBarHeightPx: Float = 0f,
    topBarOffsetY: Animatable<Float, AnimationVector1D> = Animatable(0f),
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: BookMarksViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookMarksListViewContent(
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
private fun BookMarksListViewContent(
    uiState: BookMarksUiState,
    event: BookMarksEvent?,
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
        title = stringResource(R.string.book_marks),
        navigationIcon = {
            BackIconButton(onClick = navActionManager::goBack)
        },
        scrollBehavior = topAppBarScrollBehavior,
        contentWindowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            scrolledContainerColor = TopAppBarDefaults.mediumTopAppBarColors().containerColor
        ),
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading || uiState.isLoadingMore,
            onRefresh = { event?.refreshList() },
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) {
            val listModifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .then(
                    if (nestedScrollConnection != null)
                        Modifier.nestedScroll(nestedScrollConnection)
                    else Modifier
                )
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
                    state = listState,
                    contentPadding = PaddingValues(
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        top = contentPadding.calculateTopPadding() + 8.dp,
                        end = contentPadding.calculateEndPadding(layoutDirection),
                        bottom = contentPadding.calculateBottomPadding() + 8.dp
                    ),
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
                val lazyGridState = rememberLazyGridState()
                lazyGridState.OnBottomReached(buffer = 3) {
                    event?.loadMore()
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(DEFAULT_GRID_SPAN_COUNT),
                    modifier = Modifier.fillMaxSize(),
                    state = lazyGridState,
                    contentPadding = contentPadding
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
    }
}