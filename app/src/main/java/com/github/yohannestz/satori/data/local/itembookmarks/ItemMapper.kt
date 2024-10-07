package com.github.yohannestz.satori.data.local.itembookmarks

import com.github.yohannestz.satori.data.model.volume.BookMarkItem
import com.github.yohannestz.satori.data.model.volume.Item

fun Item.toItemEntity(): ItemEntity {
    return ItemEntity(
        id = this.id,
        etag = this.etag,
        authors = this.volumeInfo.authors?.joinToString(", "),
        title = this.volumeInfo.title,
        imageUrl = this.volumeInfo.imageLinks?.thumbnail,
        smallThumbnail = this.volumeInfo.imageLinks?.smallThumbnail,
        publisher = this.volumeInfo.publisher,
        publishedDate = this.volumeInfo.publishedDate
    )
}

fun BookMarkItem.toItemEntity(): ItemEntity {
    return ItemEntity(
        id = this.id,
        etag = this.etag,
        authors = this.authors,
        title = this.title,
        imageUrl = this.imageUrl,
        smallThumbnail = this.smallThumbnail,
        publisher = this.publisher,
        publishedDate = this.publishedDate
    )
}

fun ItemEntity.toBookMarkItem(): BookMarkItem {
    return BookMarkItem(
        id = this.id,
        etag = this.etag,
        title = this.title,
        authors = this.authors,
        imageUrl = this.imageUrl,
        smallThumbnail = this.smallThumbnail,
        publisher = this.publisher,
        publishedDate = this.publishedDate
    )
}

fun List<Item>.toItemEntityList(): List<ItemEntity> {
    return map(Item::toItemEntity)
}

fun List<ItemEntity>.toBookMarkItemList(): List<BookMarkItem> {
    return map(ItemEntity::toBookMarkItem)
}