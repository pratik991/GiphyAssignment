package com.example.giphyassignment.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.giphyassignment.data.db.dao.FavoriteDao
import com.example.giphyassignment.data.db.entity.FavoriteData
import com.example.giphyassignment.data.db.entity.FavoriteItem
import com.example.giphyassignment.data.network.GiphyService
import com.example.giphyassignment.utilities.Constants.DEFAULT_PAGE_INDEX
import com.example.giphyassignment.utilities.Constants.DEFAULT_PAGE_LIMIT
import com.example.giphyassignment.utilities.Constants.NETWORK_PAGE_SIZE
import com.example.giphyassignment.utilities.Constants.PAGE_OFFSET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class GiphyPagingSource(
    private val searchQuery: String,
    private val service: GiphyService,
    private val favouritesDao: FavoriteDao,
) : PagingSource<Int, FavoriteItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavoriteItem> {
        val position = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response: FavoriteData = if (searchQuery.isEmpty()) {
                service.getTrendingGifs(DEFAULT_PAGE_LIMIT, PAGE_OFFSET)
            } else {
                service.searchForGifs(searchQuery, DEFAULT_PAGE_LIMIT, PAGE_OFFSET)
            }
            /*Increment PAGE_OFFSET for Next Fetching Next Set of Gifs*/
            PAGE_OFFSET = response.pagination?.count?.plus(response.pagination.offset!!) ?: 0
            val gifImages = gifFavImages(response)
            val nextKey = if (gifImages.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = gifImages,
                prevKey = if (position == DEFAULT_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    @OptIn(ExperimentalPagingApi::class)
    override fun getRefreshKey(state: PagingState<Int, FavoriteItem>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    /*Function for Returning Gif Images With Favourites Status*/
    private fun gifFavImages(response: FavoriteData): List<FavoriteItem> {
        val gifImages = response.data
        CoroutineScope(Dispatchers.IO).launch(block = {
            gifImages.forEach { gifImage ->
                gifImage.isFavorite = favouritesDao.isFavorite(gifImage.id)
            }
        })
        return gifImages
    }

}