package com.example.giphyassignment.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.giphyassignment.R
import com.example.giphyassignment.databinding.FragmentDashboardBinding
import com.example.giphyassignment.ui.dashboard.adapers.TabsPagerAdapter
import com.example.giphyassignment.utilities.Constants.FAVOURITES_PAGE_INDEX
import com.example.giphyassignment.utilities.Constants.TRENDING_PAGE_INDEX
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        /*Initialise Tabs Pager Adapter*/
        viewPager.adapter = TabsPagerAdapter(this)

        /*Attach the Icon & Title for Each Tab*/
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

       /*(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)*/

        return binding.root
    }

    /*Function for Setting Tab Title*/
    private fun getTabTitle(position: Int): String {
        return when (position) {
            TRENDING_PAGE_INDEX -> getString(R.string.tab_trending)
            FAVOURITES_PAGE_INDEX -> getString(R.string.tab_favorites)
            else -> throw IndexOutOfBoundsException()
        }
    }
}