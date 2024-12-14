package com.github.yohannestz.satori.ui.library.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_HEIGHT
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_WIDTH
import com.github.yohannestz.satori.ui.composables.PosterImage

@Composable
fun BookMarksListItem(
    modifier: Modifier = Modifier,
    item: BookMarkItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PosterImage(
                url = item.imageUrl?.replace("http://", "https://"),
                showShadow = false,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(MEDIA_POSTER_SMALL_WIDTH.dp)
            )

            Column(
                modifier = Modifier.heightIn(min = MEDIA_POSTER_SMALL_HEIGHT.dp),
            ) {
                Text(
                    text = if (item.authors != null) item.authors.split(",")
                        .joinToString("\n") else stringResource(R.string.unknown),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    lineHeight = 22.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = if (item.publisher != null) "${item.publisher} - ${item.publishedDate}" else item.publishedDate
                        ?: stringResource(R.string.unknown),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }
}