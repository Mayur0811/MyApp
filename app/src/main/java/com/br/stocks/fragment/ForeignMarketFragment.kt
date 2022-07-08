package com.br.stocks.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.br.stocks.R
import com.br.stocks.adapter.NewsAdapter
import com.br.stocks.databinding.FragmentForeignMarketBinding
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForeignMarketFragment : Fragment() {

    private lateinit var binding: FragmentForeignMarketBinding
    private lateinit var newsViewModel: NewsViewModel
    private var newsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentForeignMarketBinding.inflate(layoutInflater)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        if (isInternetConnected(requireContext())) {
            binding.noDataFound.visibility = View.GONE
            binding.progressCircular.visibility=ProgressBar.VISIBLE
            callApi()
        } else {
            binding.noDataFound.visibility = View.VISIBLE
            binding.rcvForeignMarketNews.visibility = View.GONE
            requireContext().toast(getString(R.string.no_internet))
        }
        return binding.root
    }

    private fun callApi() {
        newsViewModel.getForeignMarketNews(Constant.foreignMarketNews)
        lifecycle.coroutineScope.launchWhenCreated {
            newsViewModel.foreignMarketNewsResponse.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            binding.progressCircular.visibility= ProgressBar.GONE
                            it.data.response?.data?.newsList?.news?.let { newsItem ->
                                newsAdapter.setNewsItem(requireActivity(),
                                    newsItem
                                )
                            }
                            binding.rcvForeignMarketNews.visibility = View.VISIBLE
                            binding.rcvForeignMarketNews.adapter = newsAdapter
                        } else {
                            binding.progressCircular.visibility=ProgressBar.GONE
                            binding.noDataFound.visibility = View.INVISIBLE
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

        fun newInstance() = ForeignMarketFragment()

    }
}