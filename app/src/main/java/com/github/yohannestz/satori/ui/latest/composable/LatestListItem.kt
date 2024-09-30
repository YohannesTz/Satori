package com.github.yohannestz.satori.ui.latest.composable

import androidx.compose.runtime.Composable
import com.github.yohannestz.satori.data.model.volume.Item
import com.github.yohannestz.satori.ui.composables.BookListItem

@Composable
fun LatestListItem(
    item: Item,
    onClick: (Item) -> Unit
) {
    BookListItem(
        title = item.volumeInfo.title ?: "--",
        subtitle = item.volumeInfo.authors?.joinToString(", ") ?: "--",
        bottomText = item.volumeInfo.publishedDate,
        imageOverlayText = item.volumeInfo.averageRating?.let { it.toString() } ?: null,
        item.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
            ?: item.volumeInfo.imageLinks?.smallThumbnail?.replace("http://", "https://")
            ?: "",
        onClick = { onClick(item) }
    )
}