package com.example.giphyassignment.ui.trending.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.giphyassignment.data.db.entity.FavoriteItem
import com.example.giphyassignment.databinding.ItemGridGifViewBinding
import com.example.giphyassignment.databinding.ItemListGifViewBinding
import com.example.giphyassignment.ui.trending.adapters.GiphyViewHolder.FavouritesClickListener
import com.example.giphyassignment.utilities.Constants.GRID_COLUMNS


class GifImageAdapter(
    private val layoutManager: GridLayoutManager,
    private val clickListener: FavouritesClickListener
) :
    PagingDataAdapter<FavoriteItem, GiphyViewHolder>(REPO_COMPARATOR) {
    /*Diff Util For Updating The Recycler View If Any Change In Data*/
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<FavoriteItem>() {
            override fun areItemsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem): Boolean =
                oldItem == newItem
        }
    }

    enum class ViewType{
        LIST,GRID
    }
    override fun getItemViewType(position: Int): Int {
        return if (layoutManager.spanCount == GRID_COLUMNS)
            ViewType.GRID.ordinal else ViewType.LIST.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiphyViewHolder {
        return when (viewType) {
            ViewType.GRID.ordinal -> GiphyViewHolder.GridItemViewHolder(
                ItemGridGifViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), clickListener
            )
            else -> GiphyViewHolder.ListItemViewHolder(
                ItemListGifViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), clickListener
            )
        }
    }

    override fun onBindViewHolder(holder: GiphyViewHolder, position: Int) {
        /*Binding The Current Item To The View*/
        val gifImage = getItem(position)

        when (holder) {
            is GiphyViewHolder.GridItemViewHolder -> holder.bind(gifImage as FavoriteItem)
            is GiphyViewHolder.ListItemViewHolder -> holder.bind(gifImage as FavoriteItem)
        }
    }
}