package com.github.yohannestz.satori.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.composables.BackIconButton
import com.github.yohannestz.satori.ui.composables.BaseListItemPlaceHolder
import com.github.yohannestz.satori.ui.composables.OnBottomReached
import com.github.yohannestz.satori.ui.search.composable.NoResultsText
import com.github.yohannestz.satori.ui.search.composable.SearchHistoryItem
import com.github.yohannestz.satori.ui.search.composable.SearchListItem
import com.github.yohannestz.satori.utils.DEFAULT_GRID_SPAN_COUNT
import com.github.yohannestz.satori.utils.Extensions.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchHostView(
    isCompactScreen: Boolean, navActionManager: NavActionManager, padding: PaddingValues
) {
    val viewModel: SearchViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = padding.calculateTopPadding())
            .fillMaxWidth()
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = { Text(text = stringResource(R.string.search)) },
            leadingIcon = {
                if (isCompactScreen) BackIconButton(onClick = navActionManager::goBack)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                viewModel.search(query.text)
            }),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
        SearchViewContent(
            uiState = uiState,
            event = viewModel,
            query = query.text,
            onQueryChange = {
                query = TextFieldValue(
                    text = it,
                    selection = TextRange(it.length),
                )
            },
            isCompactScreen = isCompactScreen,
            navActionManager = navActionManager,
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding()),
        )
    }
}

@Composable
private fun SearchViewContent(
    uiState: SearchUiState,
    event: SearchEvent?,
    query: String,
    onQueryChange: (String) -> Unit,
    isCompactScreen: Boolean,
    navActionManager: NavActionManager,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val context = LocalContext.current
    val shouldShowPlaceholder = query.isNotBlank() && uiState.itemList.isEmpty()
    val shouldShowSearchHistory =
        (query.isBlank() || !uiState.noResult) && uiState.itemList.isEmpty() && !uiState.isLoading

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            context.showToast(uiState.message)
            event?.onMessageDisplayed()
        }
    }

    if (!isCompactScreen) {
        val listState = rememberLazyGridState()
        listState.OnBottomReached {
            event?.loadMore()
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(DEFAULT_GRID_SPAN_COUNT),
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = contentPadding
        ) {
            if (shouldShowSearchHistory) {
                items(items = uiState.searchHistoryList,
                    key = { "search_history_${it}" },
                    span = { GridItemSpan(maxLineSpan) }) { item ->
                    SearchHistoryItem(
                        item = item,
                        onClick = {
                            onQueryChange(item.query)
                            event?.search(item.query)
                        },
                        onDelete = { event?.onRemoveSearch(item) },
                        modifier = Modifier.animateItem()
                    )
                }
            }

            items(
                items = uiState.itemList,
                key = { item -> "${item.id}-${item.etag}" },
                contentType = { it.volumeInfo }
            ) { item ->
                SearchListItem(
                    item = item,
                    onClick = {
                        navActionManager.navigateToDetail(it.id)
                    }
                )
            }

            if (shouldShowPlaceholder) {
                if (uiState.isLoading) {
                    items(7) {
                        BaseListItemPlaceHolder()
                    }
                } else if (uiState.noResult) {
                    item {
                        NoResultsText()
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
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = contentPadding
        ) {
            if (shouldShowSearchHistory) {
                items(items = uiState.searchHistoryList,
                    key = { "search_history_${it}" }
                ) { item ->
                    SearchHistoryItem(
                        item = item,
                        onClick = {
                            onQueryChange(item.query)
                            event?.search(item.query)
                        },
                        onDelete = { event?.onRemoveSearch(item) },
                        modifier = Modifier.animateItem()
                    )
                }
            }

            items(
                items = uiState.itemList,
                key = { item -> "${item.id}-${item.etag}" },
                contentType = { it.volumeInfo }
            ) { item ->
                SearchListItem(
                    item = item,
                    onClick = {
                        navActionManager.navigateToDetail(it.id)
                    }
                )
            }

            if (shouldShowPlaceholder) {
                if (uiState.isLoading) {
                    items(7) {
                        BaseListItemPlaceHolder()
                    }
                } else if (uiState.noResult) {
                    item {
                        NoResultsText()
                    }
                }
            }
        }
    }
}