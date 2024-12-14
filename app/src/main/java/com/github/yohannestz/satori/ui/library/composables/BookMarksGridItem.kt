package com.github.yohannestz.satori.ui.library.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_HEIGHT
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_WIDTH
import com.github.yohannestz.satori.ui.composables.PosterImage

@Composable
fun BookMarksGridItem(
    item: BookMarkItem,
    onClick: () -> Unit,
    onRemoveIconClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .sizeIn(maxWidth = 300.dp, minWidth = 250.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PosterImage(
            url = item.imageUrl?.replace("http://", "https://"),
            showShadow = false,
            modifier = Modifier.size(
                width = MEDIA_POSTER_SMALL_WIDTH.dp,
                height = MEDIA_POSTER_SMALL_HEIGHT.dp
            )
        )
        Text(
            text = item.title ?: "--",
            fontSize = 18.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = if (item.authors != null) item.authors.split(",")
                .joinToString("\n") else stringResource(R.string.unknown),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = item.publishedDate ?: "",
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}