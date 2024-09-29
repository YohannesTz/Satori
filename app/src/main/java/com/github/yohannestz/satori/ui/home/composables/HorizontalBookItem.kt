package com.github.yohannestz.satori.ui.home.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yohannestz.satori.data.model.volume.Item
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_HEIGHT
import com.github.yohannestz.satori.ui.composables.MEDIA_POSTER_SMALL_WIDTH
import com.github.yohannestz.satori.ui.composables.PosterImage

@Composable
fun HorizontalBookItem(
    item: Item,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .sizeIn(maxWidth = 300.dp, minWidth = 250.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        PosterImage(
            url = item.volumeInfo.imageLinks.thumbnail,
            showShadow = false,
            modifier = Modifier.size(
                width = MEDIA_POSTER_SMALL_WIDTH.dp,
                height = MEDIA_POSTER_SMALL_HEIGHT.dp
            )
        )

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = item.volumeInfo.title,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = item.volumeInfo.authors?.joinToString(", ") ?: "",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}