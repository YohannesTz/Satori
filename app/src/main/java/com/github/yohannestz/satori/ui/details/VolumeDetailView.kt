package com.github.yohannestz.satori.ui.details

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.data.downloader.FileDownloadStatus
import com.github.yohannestz.satori.ui.base.navigation.NavActionManager
import com.github.yohannestz.satori.ui.composables.BackIconButton
import com.github.yohannestz.satori.ui.composables.InfoTitle
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_BIG_HEIGHT
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_BIG_WIDTH
import com.github.yohannestz.satori.ui.composables.PosterImage
import com.github.yohannestz.satori.ui.composables.ShareIconButton
import com.github.yohannestz.satori.ui.composables.SubTextIconVertical
import com.github.yohannestz.satori.ui.composables.TextSubtitleVertical
import com.github.yohannestz.satori.ui.composables.TopBannerView
import com.github.yohannestz.satori.ui.details.composables.InfoView
import com.github.yohannestz.satori.ui.details.composables.InfoViewWithContent
import com.github.yohannestz.satori.utils.Extensions.copyToClipBoard
import com.github.yohannestz.satori.utils.Extensions.defaultPlaceholder
import com.github.yohannestz.satori.utils.Extensions.htmlDecoded
import com.github.yohannestz.satori.utils.Extensions.toAnnotatedString
import com.github.yohannestz.satori.utils.UNKNOWN_CHAR

@Composable
fun VolumeDetailView(
    navActionManager: NavActionManager,
    viewModel: VolumeDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VolumeDetailViewContent(
        navActionManager = navActionManager,
        event = viewModel,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VolumeDetailViewContent(
    navActionManager: NavActionManager,
    event: VolumeDetailEvent?,
    uiState: VolumeDetailUiState
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    val isTopAppBarScrolled by remember {
        derivedStateOf { topAppBarScrollBehavior.state.collapsedFraction == 0f }
    }

    var isDescriptionExpanded by remember { mutableStateOf(false) }
    val maxLineDescription by remember {
        derivedStateOf { if (isDescriptionExpanded) Int.MAX_VALUE else 3 }
    }

    val iconExpand by remember {
        derivedStateOf {
            if (isDescriptionExpanded) R.drawable.ic_expand_less_24
            else R.drawable.ic_expand_more_24
        }
    }

    val scrollState = rememberScrollState()
    val isScrolledDown by remember {
        derivedStateOf { scrollState.value > 50 }
    }

    val context = LocalContext.current
    //val permissionLauncher = rememberLauncherForActivityResult() { }

    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    if (isTopAppBarScrolled) {
                        Text(
                            text = uiState.volume?.volumeInfo?.title ?: "Loading...",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    BackIconButton(onClick = navActionManager::goBack)
                },
                actions = {
                    if (!uiState.isLoading && uiState.volume?.volumeInfo?.canonicalVolumeLink != null) {
                        ShareIconButton(url = uiState.volume.volumeInfo.canonicalVolumeLink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                ),
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        floatingActionButton = {
            if (uiState.volume?.accessInfo?.pdf?.acsTokenLink?.isNotEmpty() == true) {
                ExtendedFloatingActionButton(
                    onClick = {
                        event?.onDownloadPDFClicked(item = uiState.volume)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_download_for_offline_24),
                            contentDescription = stringResource(R.string.download_pdf)
                        )
                    },
                    text = {
                        Text(stringResource(R.string.download_pdf))
                    },
                    expanded = isScrolledDown
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = padding.calculateBottomPadding())
                .padding(bottom = 64.dp)
        ) {
            TopBannerView(
                imageUrl = uiState.volume?.volumeInfo?.imageLinks?.thumbnail?.replace(
                    "http://",
                    "https://"
                )
                    ?: uiState.volume?.volumeInfo?.imageLinks?.smallThumbnail?.replace(
                        "http://",
                        "https://"
                    )
                    ?: "",
                modifier = Modifier.clickable {

                },
                height = padding.calculateTopPadding() + 80.dp
            )

            Row {
                PosterImage(
                    url = uiState.volume?.volumeInfo?.imageLinks?.thumbnail?.replace(
                        "http://",
                        "https://"
                    ) ?: uiState.volume?.volumeInfo?.imageLinks?.smallThumbnail?.replace(
                        "http://",
                        "https://"
                    ) ?: "",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .size(
                            width = MEDIA_POSTER_BIG_WIDTH.dp,
                            height = MEDIA_POSTER_BIG_HEIGHT.dp
                        )
                        .clickable { }
                )

                Column {
                    Text(
                        text = uiState.volume?.volumeInfo?.title
                            ?: stringResource(R.string.unknown),
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .defaultPlaceholder(uiState.isLoading),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = uiState.volume?.volumeInfo?.publishedDate
                            ?: stringResource(R.string.unknown),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .defaultPlaceholder(uiState.isLoading),
                    )


                    Text(
                        text = if (uiState.volume?.volumeInfo?.averageRating == null) ""
                        else uiState.volume.volumeInfo.averageRating.toString(),
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 2,
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .defaultPlaceholder(uiState.isLoading),
                    )

                }
            }

            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val dividerHeight = 36
                TextSubtitleVertical(
                    text = if (uiState.volume?.volumeInfo?.pageCount == null) UNKNOWN_CHAR
                    else uiState.volume.volumeInfo.pageCount.toString(),
                    subtitle = stringResource(R.string.page_count),
                    isLoading = false
                )

                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .height(dividerHeight.dp)
                )

                TextSubtitleVertical(
                    text = uiState.volume?.volumeInfo?.language ?: stringResource(R.string.unknown),
                    subtitle = stringResource(R.string.language),
                    isLoading = false
                )

                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .height(dividerHeight.dp)
                )

                SubTextIconVertical(
                    text = if (uiState.isBookMarked) stringResource(R.string.remove_from_bookmark) else stringResource(
                        R.string.add_to_bookmark
                    ),
                    icon = if (uiState.isBookMarked) R.drawable.ic_star_filled_20 else R.drawable.ic_round_star_outline_20,
                    onClick = {
                        if (uiState.isBookMarked) {
                            event?.onRemoveFromBookMarkClicked(uiState.volume)
                        } else {
                            event?.onAddToBookMarkClicked(uiState.volume)
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(
                visible = uiState.progress != null && (uiState.progress.status == FileDownloadStatus.PENDING || uiState.progress.status == FileDownloadStatus.STARTED),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (uiState.progress?.status == FileDownloadStatus.PENDING) {
                        LinearProgressIndicator(
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LinearProgressIndicator(
                            modifier = Modifier.weight(1f),
                            progress = {
                                uiState.progress?.progressInFloat() ?: 0f
                            },
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = if (uiState.progress?.status == FileDownloadStatus.PENDING) stringResource(
                            R.string.pending
                        ) else "${uiState.progress?.progressInPercentage()}%"
                    )
                }
            }

            Text(
                text = when {
                    uiState.isLoading -> buildAnnotatedString { append(stringResource(R.string.lorem_ipsun)) }
                    uiState.volume?.volumeInfo?.description != null -> {
                        uiState.volume.volumeInfo.description.htmlDecoded().toAnnotatedString()
                    }

                    else -> buildAnnotatedString { append(stringResource(R.string.not_provided)) }
                },
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                    .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                    .animateContentSize()
                    .defaultPlaceholder(uiState.isLoading),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp,
                lineHeight = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = maxLineDescription
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(48.dp))

                IconButton(
                    onClick = { isDescriptionExpanded = !isDescriptionExpanded }
                ) {
                    Icon(
                        painter = painterResource(iconExpand),
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {
                        uiState.volume?.volumeInfo?.description?.let { context.copyToClipBoard(it) }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_content_copy_24),
                        contentDescription = stringResource(R.string.copy)
                    )
                }
            }

            InfoTitle(stringResource(R.string.more_information))
            InfoView(
                title = stringResource(R.string.publisher),
                info = uiState.volume?.volumeInfo?.publisher ?: stringResource(R.string.unknown),
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )

            InfoView(
                title = stringResource(R.string.authors),
                info = uiState.volume?.volumeInfo?.authors?.joinToString("\n")
                    ?: stringResource(R.string.unknown),
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )

            InfoView(
                title = stringResource(R.string.content_version),
                info = uiState.volume?.volumeInfo?.contentVersion
                    ?: stringResource(R.string.unknown),
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )

            InfoView(
                title = stringResource(R.string.published_date),
                info = uiState.volume?.volumeInfo?.publishedDate
                    ?: stringResource(R.string.unknown),
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )

            InfoViewWithContent(
                title = stringResource(R.string.reading_modes),
                epubLink = uiState.volume?.accessInfo?.epub?.downloadLink,
                pdfLink = uiState.volume?.accessInfo?.pdf?.acsTokenLink,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            InfoView(
                title = stringResource(R.string.print_type),
                info = uiState.volume?.volumeInfo?.printType ?: stringResource(R.string.unknown),
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )

            InfoView(
                title = stringResource(R.string.maturity_rating),
                info = uiState.volume?.volumeInfo?.maturityRating
                    ?: stringResource(R.string.unknown),
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )

            Spacer(modifier = Modifier.size(48.dp))
            InfoTitle(stringResource(R.string.access_info))
            InfoView(
                title = stringResource(R.string.viewability),
                info = uiState.volume?.accessInfo?.viewability ?: stringResource(R.string.unknown),
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )

            InfoView(
                title = stringResource(R.string.is_embeddable),
                info = if (uiState.volume?.accessInfo?.embeddable == true) stringResource(R.string.yes) else stringResource(
                    R.string.no
                ),
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )

            InfoView(
                title = stringResource(R.string.public_domain),
                info = if (uiState.volume?.accessInfo?.publicDomain == true) stringResource(R.string.yes) else stringResource(
                    R.string.no
                ),
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 8.dp)
                    .defaultPlaceholder(uiState.isLoading)
            )
        }
    }
}