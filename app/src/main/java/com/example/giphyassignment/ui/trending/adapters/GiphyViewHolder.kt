package com.example.giphyassignment.ui.trending.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieAnimationView
import com.example.giphyassignment.data.db.entity.FavoriteItem
import com.example.giphyassignment.databinding.ItemGridGifViewBinding
import com.example.giphyassignment.databinding.ItemListGifViewBinding
import com.example.giphyassignment.ui.common.favouriteMarker
import com.example.giphyassignment.vibrate


sealed class GiphyViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    /*Grid Item View Holder for Trending Gifs*/
    class GridItemViewHolder(
        private val binding: ItemGridGifViewBinding,
        private val clickListener: FavouritesClickListener,
    ) : GiphyViewHolder(binding) {
        fun bind(gifImage: FavoriteItem) {
            binding.gifImage = gifImage
            binding.favourite = clickListener
        }
    }

    /*List Item View Holder For Searched Lists*/
    class ListItemViewHolder(
        private val binding: ItemListGifViewBinding,
        private val clickListener: FavouritesClickListener,
    ) : GiphyViewHolder(binding) {
        fun bind(gifImage: FavoriteItem) {
            binding.gifImage = gifImage
            binding.favourite = clickListener
        }
    }

    /*Favourites Gif Click Listener*/
    class FavouritesClickListener(val clickListener: (favoriteGifInfo: FavoriteItem) -> Unit) {
        fun onToggle(favView: View, favoriteGifInfo: FavoriteItem) {
            vibrate(favView)
            clickListener(favoriteGifInfo)
            favoriteGifInfo.isFavorite = favoriteGifInfo.isFavorite.not()
            favouriteMarker(favView as LottieAnimationView, favoriteGifInfo.isFavorite)
        }
    }
}