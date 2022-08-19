package com.example.giphyassignment.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class FavoriteData(
    val data: List<FavoriteItem>,
    val pagination: Pagination?
)

data class Pagination(
    val count: Int?,
    val offset: Int?,
    val total_count: Int?
)

@Entity(tableName = "favourites_table")
data class FavoriteItem(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @Embedded(prefix = "gif_") val images: GifImages,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean
)

data class GifImages(
    @Embedded(prefix = "image_") val fixed_height: GifImage
)

data class GifImage(
    @ColumnInfo(name = "url") val url: String
)