package com.example.giphyassignment.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.giphyassignment.R
import com.example.giphyassignment.data.db.entity.FavoriteItem
import com.example.giphyassignment.databinding.FragmentTrendingBinding
import com.example.giphyassignment.snapToPosition
import com.example.giphyassignment.ui.common.LoaderStateAdapter
import com.example.giphyassignment.ui.trending.adapters.GifImageAdapter
import com.example.giphyassignment.ui.trending.adapters.GiphyViewHolder.FavouritesClickListener
import com.example.giphyassignment.utilities.Constants.GRID_COLUMNS
import com.example.giphyassignment.utilities.Constants.LIST_COLUMN
import com.example.giphyassignment.utilities.Constants.PAGE_OFFSET
import com.example.giphyassignment.viewmodel.GiphyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TrendingFragment : Fragment() {
    private lateinit var binding: FragmentTrendingBinding
    private val giphyViewModel: GiphyViewModel by viewModels()

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: GifImageAdapter

    /*Search Gif Query*/
    private var searchGifs: String = String()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrendingBinding.inflate(inflater, container, false)
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        /*Initialise Gif Recycler View*/
        initGifRecyclerView()
        /*Search for Gifs Using Search View*/
        gifSearcher()
        /*Swipe Refresh Reload Action*/
        swipeRefreshGifs()
        /*Track Data Flow Progress States*/
        trackProgressStates()
    }

    /*Function for Initialising Gif Recycler View*/
    private fun initGifRecyclerView() {
        layoutManager = GridLayoutManager(context, GRID_COLUMNS)
        binding.rvGiphy.layoutManager = layoutManager
        /*Gif Images Adapter For Displaying Gif Images*/
        adapter = GifImageAdapter(layoutManager, FavouritesClickListener {
            onFavouriteClicked(it)
        })
        /*Loader Adapter For Indicating Error & Retry Events*/
        binding.rvGiphy.adapter = adapter.withLoadStateFooter(
            footer = LoaderStateAdapter { adapter.retry() }
        )
    }

    /*Search For Gif Images*/
    private fun gifSearcher() {
        binding.svGifs.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchQuery: String): Boolean {
                binding.svGifs.clearFocus()
                searchGifs = searchQuery
                reloadGifData()
                return false
            }

            override fun onQueryTextChange(searchQuery: String): Boolean {
                searchGifs = searchQuery
                reloadGifData()
                return false
            }
        })
    }

    /*Pull To Refresh For Refreshing Gif Data*/
    private fun swipeRefreshGifs() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.svGifs.setQuery(String(), false)
            binding.svGifs.clearFocus()
            reloadGifData()
        }
    }

    /*Function for Fetching Fresh Gif Info*/
    private fun reloadGifData() {
        PAGE_OFFSET = 0
        fetchGifImages()
        giphyLayoutManager()
    }

    /*Function for Fetching Trending Gif Images or Search Gif Images If Search Query Present*/
    private fun fetchGifImages() {
        lifecycleScope.launch {
            giphyViewModel.fetchGifImages(searchGifs).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    /*Reload Layout Manager When Search Action Performed*/
    private fun giphyLayoutManager() {
        if (searchGifs.isEmpty()) layoutManager.spanCount =
            GRID_COLUMNS else layoutManager.spanCount = LIST_COLUMN
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        binding.rvGiphy.snapToPosition(0)
    }

    /*Mark/UnMark Gif As Favourites*/
    private fun onFavouriteClicked(favoriteGifInfo: FavoriteItem) {
        lifecycleScope.launch {
            giphyViewModel.toggleFavourites(favoriteGifInfo)
        }
    }

    /*Function for Tracking The Progress States*/
    private fun trackProgressStates() {
        lifecycleScope.launch {
            adapter.addLoadStateListener { loadState ->
                // Show loading spinner during initial load or refresh
                binding.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
                // Only show the list if refresh succeeds
                binding.rvGiphy.isVisible = loadState.refresh is LoadState.NotLoading
                        || loadState.refresh !is LoadState.Error
                // Display Empty View For No Records Found*/
                val isEmptyList = loadState.refresh is LoadState.Error
                        || loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.tvError.isVisible = isEmptyList
                if (isEmptyList) showEmptyState(loadState.refresh)
            }
        }
    }

    /*Function for Displaying Empty State With Message*/
    private fun showEmptyState(loadState: LoadState) {
        binding.tvError.text = getString(R.string.no_records_found)
        // Show the error state if initial load or refresh fails
        if (loadState is LoadState.Error) binding.tvError.text = getString(R.string.label_error)
    }

    override fun onResume() {
        super.onResume()
        reloadGifData()
    }
}