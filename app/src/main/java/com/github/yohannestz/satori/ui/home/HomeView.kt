package com.github.yohannestz.satori.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.data.model.VolumeCategory
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.base.navigation.Route
import com.github.yohannestz.satori.ui.composables.HorizontalListHeader
import com.github.yohannestz.satori.ui.composables.HorizontalPlaceHolder
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_HEIGHT
import com.github.yohannestz.satori.ui.home.composables.CategoriesCard
import com.github.yohannestz.satori.ui.home.composables.HorizontalBookItem
import com.github.yohannestz.satori.ui.theme.SatoriTheme
import com.github.yohannestz.satori.utils.Extensions.collapsable
import com.github.yohannestz.satori.utils.Extensions.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeView(
    navActionManager: NavActionManager,
    topBarHeightPx: Float,
    topBarOffsetY: Animatable<Float, AnimationVector1D>,
    padding: PaddingValues,
) {
    val viewModel: HomeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeViewContent(
        uiState = uiState,
        event = viewModel,
        navActionManager = navActionManager,
        topBarHeightPx = topBarHeightPx,
        topBarOffsetY = topBarOffsetY,
        padding = padding
    )
}

@Composable
private fun HomeViewContent(
    uiState: HomeUiState,
    event: HomeEvent?,
    navActionManager: NavActionManager,
    topBarHeightPx: Float,
    topBarOffsetY: Animatable<Float, AnimationVector1D> = Animatable(0f),
    padding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val historyListState = rememberLazyListState()
    val biographyListState = rememberLazyListState()
    val fictionListState = rememberLazyListState()
    val selfHelpListState = rememberLazyListState()

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            context.showToast(uiState.message)
            event?.onMessageDisplayed()
        }
    }

    LaunchedEffect(Unit) {
        event?.initRequestChain()
    }

    Column(
        modifier = Modifier
            .collapsable(
                state = scrollState,
                topBarHeightPx = topBarHeightPx,
                topBarOffsetY = topBarOffsetY
            )
            .verticalScroll(scrollState)
            .padding(padding)
    ) {
        Row(
            modifier = Modifier.padding(top = 10.dp, start = 8.dp, end = 16.dp)
        ) {
            CategoriesCard(
                text = stringResource(R.string.philosophy),
                icon = R.drawable.ic_book_spark_4,
                modifier = Modifier.weight(1f),
                onClick = dropUnlessResumed {
                    navActionManager.navigateTo(Route.VolumeList(VolumeCategory.PHILOSOPHY))
                }
            )

            CategoriesCard(
                text = stringResource(R.string.business_economics),
                icon = R.drawable.ic_book_spark_4,
                modifier = Modifier.weight(1f),
                onClick = dropUnlessResumed {
                    navActionManager.navigateTo(Route.VolumeList(VolumeCategory.BUSINESS_ECONOMICS))
                }
            )
        }

        Row(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 16.dp)
        ) {
            CategoriesCard(
                text = stringResource(R.string.science_math),
                icon = R.drawable.ic_book_spark_4,
                modifier = Modifier.weight(1f),
                onClick = dropUnlessResumed {
                    navActionManager.navigateTo(Route.VolumeList(VolumeCategory.SCIENCE_MATH))
                }
            )

            CategoriesCard(
                text = stringResource(R.string.computer_technology),
                icon = R.drawable.ic_book_spark_4,
                modifier = Modifier.weight(1f),
                onClick = dropUnlessResumed {
                    navActionManager.navigateTo(Route.VolumeList(VolumeCategory.COMPUTER_TECHNOLOGY))
                }
            )
        }

        HorizontalListHeader(
            text = stringResource(R.string.self_help),
            onClick = dropUnlessResumed {
                navActionManager.navigateTo(Route.VolumeList(VolumeCategory.SELF_HELP))
            }
        )

        if (!uiState.isLoading && uiState.selfHelpBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MEDIA_POSTER_SMALL_HEIGHT.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.we_couldnt_find),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .sizeIn(minHeight = MEDIA_POSTER_SMALL_HEIGHT.dp),
                state = selfHelpListState,
                contentPadding = PaddingValues(horizontal = 8.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = selfHelpListState)
            ) {
                items(
                    items = uiState.selfHelpBooks,
                    key = { it.id },
                    contentType = { it.volumeInfo }
                ) {
                    HorizontalBookItem(
                        item = it,
                        onClick = dropUnlessResumed {
                            navActionManager.navigateToDetail(it.id)
                        }
                    )
                }

                if (uiState.isLoading) {
                    items(5) {
                        HorizontalPlaceHolder()
                    }
                }
            }
        }

        HorizontalListHeader(
            text = stringResource(R.string.history),
            onClick = dropUnlessResumed {
                navActionManager.navigateTo(Route.VolumeList(VolumeCategory.HISTORY))
            }
        )

        if (!uiState.isLoading && uiState.historyBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MEDIA_POSTER_SMALL_HEIGHT.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.we_couldnt_find),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .sizeIn(minHeight = MEDIA_POSTER_SMALL_HEIGHT.dp),
                state = historyListState,
                contentPadding = PaddingValues(horizontal = 8.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = historyListState)
            ) {
                items(
                    items = uiState.historyBooks,
                    key = { it.id },
                    contentType = { it.volumeInfo }
                ) {
                    HorizontalBookItem(
                        item = it,
                        onClick = dropUnlessResumed {
                            navActionManager.navigateToDetail(it.id)
                        }
                    )
                }

                if (uiState.isLoading) {
                    items(5) {
                        HorizontalPlaceHolder()
                    }
                }
            }
        }

        HorizontalListHeader(
            text = stringResource(R.string.biography),
            onClick = dropUnlessResumed {
                navActionManager.navigateTo(Route.VolumeList(VolumeCategory.BIOGRAPHY))
            }
        )

        if (!uiState.isLoading && uiState.biographyBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MEDIA_POSTER_SMALL_HEIGHT.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.we_couldnt_find),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .sizeIn(minHeight = MEDIA_POSTER_SMALL_HEIGHT.dp),
                state = biographyListState,
                contentPadding = PaddingValues(horizontal = 8.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = biographyListState)
            ) {
                items(
                    items = uiState.biographyBooks,
                    key = { it.id },
                    contentType = { it.volumeInfo }
                ) {
                    HorizontalBookItem(
                        item = it,
                        onClick = dropUnlessResumed {
                            navActionManager.navigateToDetail(it.id)
                        }
                    )
                }

                if (uiState.isLoading) {
                    items(5) {
                        HorizontalPlaceHolder()
                    }
                }
            }
        }

        HorizontalListHeader(
            text = stringResource(R.string.fiction),
            onClick = dropUnlessResumed {
                navActionManager.navigateTo(Route.VolumeList(VolumeCategory.FICTION))
            }
        )

        if (!uiState.isLoading && uiState.fictionBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MEDIA_POSTER_SMALL_HEIGHT.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.we_couldnt_find),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .sizeIn(minHeight = MEDIA_POSTER_SMALL_HEIGHT.dp),
                state = fictionListState,
                contentPadding = PaddingValues(horizontal = 8.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = fictionListState)
            ) {
                items(
                    items = uiState.fictionBooks,
                    key = { it.id },
                    contentType = { it.volumeInfo }
                ) {
                    HorizontalBookItem(
                        item = it,
                        onClick = dropUnlessResumed {
                            navActionManager.navigateToDetail(it.id)
                        }
                    )
                }

                if (uiState.isLoading) {
                    items(5) {
                        HorizontalPlaceHolder()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    SatoriTheme {
        Surface {
            HomeViewContent(
                uiState = HomeUiState(),
                event = null,
                navActionManager = NavActionManager.rememberNavActionManager(),
                topBarHeightPx = 0f
            )
        }
    }
}