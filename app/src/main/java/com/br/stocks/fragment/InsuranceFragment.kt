package com.br.stocks.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.br.stocks.R
import com.br.stocks.adapter.InsuranceNewsAdapter
import com.br.stocks.databinding.FragmentInsuranceBinding
import com.br.stocks.models.InsuranceNewsItem
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.InsuranceViewModel
import com.br.stocks.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InsuranceFragment : Fragment() {

    private lateinit var binding: FragmentInsuranceBinding
    private lateinit var viewModel: InsuranceViewModel
    private var newsAdapter = InsuranceNewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInsuranceBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[InsuranceViewModel::class.java]
        if (isInternetConnected(requireContext())) {
            binding.noDataFound.visibility = View.GONE
            binding.progressCircular.visibility=ProgressBar.VISIBLE
            callApi()
        }else{
            binding.noDataFound.visibility = View.VISIBLE
            binding.rcvInsuranceNews.visibility = View.GONE
            requireContext().toast(getString(R.string.no_internet))
        }

        initView()
        return binding.root
    }

    private fun initView() {
        newsAdapter.onNewsItemClick = {
            showDialog(it)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun showDialog(insuranceNewsItem: InsuranceNewsItem) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.insurance_description)
        val title = dialog.findViewById<TextView>(R.id.txtCaption)
        val desc = dialog.findViewById<TextView>(R.id.txtNews)
        val close = dialog.findViewById<ImageButton>(R.id.btnCancel)

        title.text = insuranceNewsItem.caption
        desc.text = Html.fromHtml(insuranceNewsItem.news)
        close.setOnClickListener { dialog.dismiss() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.show()

    }

    private fun callApi() {
        viewModel.getInsuranceNews(Constant.insuranceNews)
        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.insuranceNewsResponse.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            it.data.response?.data?.insuranceNewsList?.insuranceNews?.let { newsItem ->
                                newsAdapter.setNewsItem(requireActivity(), newsItem)
                            }
                            binding.rcvInsuranceNews.visibility = View.VISIBLE
                            binding.rcvInsuranceNews.adapter = newsAdapter
                            binding.progressCircular.visibility= ProgressBar.GONE
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
        fun newInstance() = InsuranceFragment()
    }
}