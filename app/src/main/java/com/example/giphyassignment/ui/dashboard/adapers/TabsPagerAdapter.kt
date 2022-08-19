package com.example.giphyassignment.ui.dashboard.adapers

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.giphyassignment.ui.favorites.FavoritesFragment
import com.example.giphyassignment.ui.trending.TrendingFragment
import com.example.giphyassignment.utilities.Constants.FAVOURITES_PAGE_INDEX
import com.example.giphyassignment.utilities.Constants.TRENDING_PAGE_INDEX


class TabsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        TRENDING_PAGE_INDEX to { TrendingFragment() },
        FAVOURITES_PAGE_INDEX to { FavoritesFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}