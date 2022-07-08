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
import com.br.stocks.databinding.FragmentIPOBinding
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IPOFragment : Fragment() {

    private lateinit var binding:FragmentIPOBinding
    private lateinit var newsViewModel: NewsViewModel
    private var newsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding= FragmentIPOBinding.inflate(layoutInflater)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        if (isInternetConnected(requireContext())) {
            binding.noDataFound.visibility = View.GONE
            binding.progressCircular.visibility=ProgressBar.VISIBLE
            callApi()
        }else{
            binding.noDataFound.visibility = View.VISIBLE
            binding.rcvIPONews.visibility =View.GONE
            requireContext().toast(getString(R.string.no_internet))
        }
        return binding.root
    }

    private fun callApi() {
        newsViewModel.getIPONews(Constant.ipoNews)
        lifecycle.coroutineScope.launchWhenCreated {
            newsViewModel.ipoNewsResponse.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            binding.progressCircular.visibility= ProgressBar.GONE
                            it.data.response?.data?.newsList?.news?.let { newsItem ->
                                newsAdapter.setNewsItem(requireActivity(), newsItem)
                            }
                            binding.rcvIPONews.visibility = View.VISIBLE
                            binding.rcvIPONews.adapter = newsAdapter
                        }else{
                            binding.progressCircular.visibility=ProgressBar.GONE
                            binding.noDataFound.visibility = View.VISIBLE
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
        fun newInstance() = IPOFragment()
    }
}