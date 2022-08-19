package com.example.giphyassignment.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giphyassignment.data.db.entity.FavoriteItem

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favourites_table")
    fun getFavorites(): PagingSource<Int, FavoriteItem>

    @Query("SELECT EXISTS (SELECT * FROM favourites_table WHERE id = :gifId)")
    suspend fun isFavorite(gifId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favoriteGifInfo: FavoriteItem)

    @Query("DELETE FROM favourites_table WHERE id = :gifId")
    suspend fun removeFavourite(gifId: String)
}