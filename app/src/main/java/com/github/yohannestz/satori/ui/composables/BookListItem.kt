package com.github.yohannestz.satori.ui.composables

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.utils.Extensions.defaultPlaceholder


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookListItem(
    title: String,
    subtitle: String,
    bottomText: String? = null,
    imageOverlayText: String? = null,
    imageUrl: String? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .combinedClickable(onLongClick = onLongClick, onClick = onClick),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.BottomStart
            ) {
                PosterImage(
                    url = imageUrl,
                    showShadow = false,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(MEDIA_POSTER_SMALL_WIDTH.dp)
                )

                imageOverlayText?.let {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topEnd = 8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it, modifier = Modifier.padding(
                                start = 8.dp, top = 4.dp, end = 2.dp, bottom = 4.dp
                            ), color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Icon(
                            painter = painterResource(R.drawable.ic_round_star_24),
                            contentDescription = "star",
                            modifier = Modifier.padding(end = 4.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.heightIn(min = MEDIA_POSTER_SMALL_HEIGHT.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = title,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    Text(
                        text = subtitle,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column {
                    bottomText?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookListItemPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier.height(MEDIA_POSTER_SMALL_HEIGHT.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(
                        width = MEDIA_POSTER_SMALL_WIDTH.dp, height = MEDIA_POSTER_SMALL_HEIGHT.dp
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .defaultPlaceholder(visible = true)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Title Placeholder text here",
                    modifier = Modifier.defaultPlaceholder(visible = true),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = "Subtitle Placeholder",
                    modifier = Modifier.defaultPlaceholder(visible = true),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Bottom Text",
                    modifier = Modifier.defaultPlaceholder(visible = true),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun BookListItemPreview() {
    MaterialTheme {
        Column {
            BookListItem(
                title = "Example Book Title",
                subtitle = "Example Subtitle",
                bottomText = "Published: 2024",
                imageOverlayText = "5.2",
                imageUrl = null,
                onClick = { },
                onLongClick = { }
            )
            BookListItemPlaceholder()
        }
    }
}

