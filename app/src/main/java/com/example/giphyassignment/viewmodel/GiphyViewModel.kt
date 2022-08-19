package com.example.giphyassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.giphyassignment.data.db.entity.FavoriteItem
import com.example.giphyassignment.data.repository.GiphyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiphyViewModel @Inject constructor(
    private val repository: GiphyRepository
) : ViewModel() {

    /*Fetch Gif Images Using Pagination Trending & Search*/
    fun fetchGifImages(searchGifs: String): Flow<PagingData<FavoriteItem>> {
        return repository.letGifImagesFlow(searchGifs)
            .map { it.map { it } }
            .cachedIn(viewModelScope)
    }

    /*Fetch My Favourite Gif Images Using Pagination*/
    fun fetchMyFavourites(): Flow<PagingData<FavoriteItem>> {
        return repository.letMyFavouritesFlow()
            .map { it.map { it } }
            .cachedIn(viewModelScope)
    }

    /*Adding/Removing Gif Image To Favourites*/
    fun toggleFavourites(favoriteGifInfo: FavoriteItem) {
        viewModelScope.launch {
            when {
                isFavorite(favoriteGifInfo) -> removeFromFavourites(favoriteGifInfo.id)
                else -> addToFavourites(favoriteGifInfo)
            }
        }
    }

    /*Adding Gif Image To Favourites*/
    private fun addToFavourites(favoriteGifInfo: FavoriteItem) {
        viewModelScope.launch {
            repository.markFavourite(favoriteGifInfo)
        }
    }

    /*Removing Gif Image From Favourites*/
    private fun removeFromFavourites(gifId: String) {
        viewModelScope.launch {
            repository.unMarkFavourite(gifId)
        }
    }

    /*Check If The Gif Image Is Favourite Or Not*/
    private suspend fun isFavorite(favoriteGifInfo: FavoriteItem): Boolean {
        return repository.isFavorite(favoriteGifInfo)
    }
}