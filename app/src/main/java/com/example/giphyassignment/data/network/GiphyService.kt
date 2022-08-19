package com.example.giphyassignment.data.network

import com.example.giphyassignment.data.db.entity.FavoriteData
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {
    @GET("trending")
    suspend fun getTrendingGifs(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): FavoriteData

    @GET("search")
    suspend fun searchForGifs(
        @Query("q") searchQuery: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): FavoriteData
}