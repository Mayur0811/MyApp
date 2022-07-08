package com.br.stocks.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.br.stocks.R
import com.br.stocks.adapter.PagerAdapter
import com.br.stocks.databinding.FragmentNewsBinding
import com.br.stocks.utils.CommonUtils
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        initView()

        return binding.root
    }

    private fun initView() {
        initPagerAdapter()
    }

    private fun initPagerAdapter() {
        val pagerAdapter = fragmentManager?.let { PagerAdapter(it) }
        pagerAdapter?.addFragment(PreSessionFragment(),getString(R.string.pre_session))
        pagerAdapter?.addFragment(EconomyFragment(), getString(R.string.economy))
        pagerAdapter?.addFragment(InsuranceFragment(), getString(R.string.insuarnce))
        pagerAdapter?.addFragment(MutualFundFragment(), getString(R.string.mutual_funds))
        pagerAdapter?.addFragment(IPOFragment(), getString(R.string.ipo))
        pagerAdapter?.addFragment(ForeignMarketFragment(), getString(R.string.foreign_market))
        binding.vpNews.adapter = pagerAdapter
        binding.tabs.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabs.setupWithViewPager(binding.vpNews)


    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}