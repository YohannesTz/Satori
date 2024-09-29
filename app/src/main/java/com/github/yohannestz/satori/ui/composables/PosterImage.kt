package com.github.yohannestz.satori.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

const val MEDIA_POSTER_COMPACT_HEIGHT = 100
const val MEDIA_POSTER_COMPACT_WIDTH = 100

const val MEDIA_POSTER_SMALL_HEIGHT = 140
const val MEDIA_POSTER_SMALL_WIDTH = 100

const val MEDIA_POSTER_MEDIUM_HEIGHT = 156
const val MEDIA_POSTER_MEDIUM_WIDTH = 110

const val MEDIA_POSTER_BIG_HEIGHT = 213
const val MEDIA_POSTER_BIG_WIDTH = 150

@Composable
fun PosterImage(
    url: String?,
    showShadow: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = "poster_image",
        contentScale = contentScale,
        placeholder = ColorPainter(MaterialTheme.colorScheme.outline),
        error = ColorPainter(MaterialTheme.colorScheme.outline),
        fallback = ColorPainter(MaterialTheme.colorScheme.outline),
        modifier = modifier
            .then(
                if (showShadow) Modifier
                    .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 8.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                else Modifier
            )
            .clip(RoundedCornerShape(8.dp))
    )
}

@Preview(showBackground = true)
@Composable
private fun PosterImagePreview() {
    PosterImage(
        url = "https://example.com/sample-poster.jpg",
        showShadow = true,
        modifier = Modifier
            .size(width = 120.dp, height = 180.dp)
    )
}
