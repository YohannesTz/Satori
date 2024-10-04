package com.github.yohannestz.satori.ui.search.composable


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yohannestz.satori.data.model.volume.Item
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_HEIGHT
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_WIDTH
import com.github.yohannestz.satori.ui.composables.PosterImage
import com.github.yohannestz.satori.utils.Extensions.defaultPlaceholder

@Composable
fun SearchListGridItem(
    item: Item,
    onClick: () -> Unit
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
            url = item.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
                ?: item.volumeInfo.imageLinks?.smallThumbnail?.replace("http://", "https://")
                ?: "",
            showShadow = false,
            modifier = Modifier.size(
                width = MEDIA_POSTER_SMALL_WIDTH.dp,
                height = MEDIA_POSTER_SMALL_HEIGHT.dp
            )
        )
        Text(
            text = item.volumeInfo.title ?: "--",
            fontSize = 18.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = item.volumeInfo.authors?.joinToString(", ") ?: "",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = item.volumeInfo.publishedDate ?: "",
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun SearchListGridItemPlaceHolder() {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .sizeIn(maxWidth = 300.dp, minWidth = 250.dp)
            .clip(RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = MEDIA_POSTER_SMALL_WIDTH.dp,
                    height = MEDIA_POSTER_SMALL_HEIGHT.dp
                )
                .clip(RoundedCornerShape(8.dp))
                .defaultPlaceholder(visible = true)
        )

        Text(
            text = "This is going to be the title",
            fontSize = 18.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
                .defaultPlaceholder(visible = true)
        )

        Text(
            text = "This is going to be the author",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
                .defaultPlaceholder(visible = true)
        )

        Text(
            text = "placeholder text",
            color = MaterialTheme.colorScheme.outline,
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
                .defaultPlaceholder(visible = true)
        )
    }
}