package com.example.giphyassignment.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.giphyassignment.data.db.dao.FavoriteDao
import com.example.giphyassignment.data.db.entity.FavoriteItem
import com.example.giphyassignment.data.network.GiphyService
import com.example.giphyassignment.data.remote.GiphyPagingSource
import com.example.giphyassignment.utilities.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiphyRepository @Inject constructor(
    private val service: GiphyService,
    private val favouritesDao: FavoriteDao
) {
    /*Fetch Gif Images Using Pagination Trending & Search*/
    fun letGifImagesFlow(
        searchGifs: String,
        pagingConfig: PagingConfig = getDefaultPageConfig()
    ): Flow<PagingData<FavoriteItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { GiphyPagingSource(searchGifs, service, favouritesDao) }
        ).flow
    }

    /*Fetch My Favourite Gif Images Using Pagination*/
    fun letMyFavouritesFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig()
    ): Flow<PagingData<FavoriteItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { favouritesDao.getFavorites() }
        ).flow
    }

    /**
     * Let's define page size, page size is the only required param
     */
    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = true)
    }

    /**
     * Marking Gif Images To Favourites
     */
    suspend fun isFavorite(favoriteGifInfo: FavoriteItem): Boolean {
        return favouritesDao.isFavorite(favoriteGifInfo.id)
    }

    /**
     * Marking Gif Images To Favourites
     */
    suspend fun markFavourite(favoriteGifInfo: FavoriteItem) {
        favouritesDao.addFavorite(favoriteGifInfo)
    }

    /**
     * Removing Gif Images To Favourites
     */
    suspend fun unMarkFavourite(gifId: String) {
        favouritesDao.removeFavourite(gifId)
    }

}