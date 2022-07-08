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
import com.br.stocks.adapter.PreSessionAdapter
import com.br.stocks.databinding.FragmentPreSessionBinding
import com.br.stocks.models.New
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.PreSessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreSessionFragment : Fragment() {

    private lateinit var preSessionAdapter: PreSessionAdapter
    lateinit var binding: FragmentPreSessionBinding
    private lateinit var viewModel: PreSessionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPreSessionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[PreSessionViewModel::class.java]
        preSessionAdapter = PreSessionAdapter()

        if (isInternetConnected(requireContext())) {
            binding.noDataFound.visibility = View.GONE
            binding.progressCircular.visibility=ProgressBar.VISIBLE
            callApi()
        } else {
            binding.noDataFound.visibility = View.VISIBLE
            requireContext().toast(getString(R.string.no_internet))
            binding.preSessionList.visibility = View.GONE

        }
        return binding.root
    }

    private fun callApi() {
        viewModel.getPreSession(Constant.preSession)
        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.preSession.collect { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        if (response.data != null && response.data.response?.data?.newsList?.news?.isNotEmpty() == true) {
                           binding.progressCircular.visibility=ProgressBar.GONE
                            response.data.response.data.newsList.news.let {
                                preSessionAdapter.setPreSessionItem(
                                    requireActivity(),
                                    it as ArrayList<New>
                                )
                            }
                            binding.progressCircular.visibility=ProgressBar.GONE
                            binding.preSessionList.visibility = View.VISIBLE
                            binding.preSessionList.adapter = preSessionAdapter
                        }else{
                            binding.progressCircular.visibility=ProgressBar.GONE
                            binding.noDataFound.visibility = View.VISIBLE
                        }
                    }
                    is NetworkResult.Loading -> {
                    }
                    is NetworkResult.Error -> {
                        binding.progressCircular.visibility = ProgressBar.GONE
                        binding.noDataFound.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }
    }

}

