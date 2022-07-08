package com.br.stocks.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.br.stocks.R
import com.br.stocks.adapter.NewsAdapter
import com.br.stocks.databinding.FragmentEconomyBinding
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class
EconomyFragment : Fragment() {

    private lateinit var binding: FragmentEconomyBinding
    private lateinit var newsViewModel: NewsViewModel
    private var economyNewsAdapter = NewsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEconomyBinding.inflate(layoutInflater)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        if (isInternetConnected(requireContext())) {
            binding.noDataFound.visibility = View.GONE
            binding.progressCircular.visibility=ProgressBar.VISIBLE
            callApi()
        } else {
            binding.rcvEconomyNewsList.visibility=View.GONE
            binding.noDataFound.visibility = View.VISIBLE
            requireContext().toast(getString(R.string.no_internet))
        }

        return binding.root
    }

    private fun callApi() {
        newsViewModel.getEconomyNews(Constant.economyNews)
        lifecycle.coroutineScope.launchWhenCreated {
            newsViewModel.economyNewsResponse.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data != null ) {
                            it.data.response?.data?.newsList?.news?.let { newsItem ->
                                economyNewsAdapter.setNewsItem(requireActivity(),
                                    newsItem
                                )
                            }
                            binding.progressCircular.visibility= ProgressBar.GONE
                            binding.rcvEconomyNewsList.visibility = View.VISIBLE
                            binding.rcvEconomyNewsList.adapter = economyNewsAdapter
                        }else{
                            binding.noDataFound.visibility = View.VISIBLE
                            binding.progressCircular.visibility=ProgressBar.GONE
                        }
                    }
                    is NetworkResult.Error -> {
                        binding.progressCircular.visibility=ProgressBar.GONE
                        binding.noDataFound.visibility = View.VISIBLE
                    }
                    is NetworkResult.Loading -> {

                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        fun newInstance() = EconomyFragment()
    }

}
