package com.br.stocks.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.br.stocks.BuildConfig
import com.br.stocks.R
import com.br.stocks.adapter.CarouselAdapter
import com.br.stocks.adapter.StockDataAdapter
import com.br.stocks.databinding.FragmentHomeBinding
import com.br.stocks.models.DataItem
import com.br.stocks.utils.CommonUtils
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.PreferenceUtil
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.HomeViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private var carouselAdapter = CarouselAdapter()
    private var blueChipAdapter = StockDataAdapter()
    private var midCapAdapter = StockDataAdapter()
    private var smallCapAdapter = StockDataAdapter()
    private var mInterstitialAd: InterstitialAd? = null
    private var isFromMore: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        if (isInternetConnected(requireContext())) {
            binding.noDataFound.visibility = View.GONE
            initApi()
        } else {
            requireContext().toast(getString(R.string.no_internet))
        }

        binding.swipeToRefresh.setOnRefreshListener {
            if (isInternetConnected(requireContext())) {
                initApi()
            } else {
                requireContext().toast(getString(R.string.no_internet))
                binding.swipeToRefresh.isRefreshing = false
            }
        }
        initView()
        return binding.root
    }

    private fun initView() {
        try {
            val manager = context?.let { ReviewManagerFactory.create(it) }
            val request = manager?.requestReviewFlow()
            request?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = task.result
                    val flow =
                        activity?.let {
                            reviewInfo?.let { it1 ->
                                manager.launchReviewFlow(
                                    it,
                                    it1
                                )
                            }
                        }
                    flow?.addOnCompleteListener { _ ->

                    }
                } else { }
            }
        }catch (e:Exception){
            Log.d(TAG, "initView: ${e.message}")
        }
        initData()
        initCarouselView()
        initAdapter()
        initOnClick()
        initInterstitialAd()

    }

    private fun initOnClick() {

        binding.loutBlueChipMore.setOnClickListener {
            isFromMore = true
            showInterstitialAd(Constant.blueChipCategory, getString(R.string.blue_chip_breakout))
        }

        binding.loutMidCapMore.setOnClickListener {
            isFromMore = true
            showInterstitialAd(Constant.midCapCategory, getString(R.string.midcap_breakout))
        }

        binding.loutSmallCapMore.setOnClickListener {
            isFromMore = true
            showInterstitialAd(Constant.smallCapCategory, getString(R.string.smallcap_breakout))
        }

    }

    private fun initInterstitialAd(
    ) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),
            CommonUtils.getInterstitialAdsKey(),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, "onAdFailedToLoad: $adError")

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })


    }

    private fun showInterstitialAd(
        category: String? = null,
        categoryTitle: String? = null,
        dataItem: DataItem? = null,
    ) {
        if (mInterstitialAd != null) {
            if (Constant.showAd_description && Constant.descriptionLoadAd == 0 && isFromMore == false) {
                mInterstitialAd?.show(requireActivity())

            } else if (Constant.stockByCategory_showAd && Constant.moreLoadAd == 0 && isFromMore == true) {
                mInterstitialAd?.show(requireActivity())
            } else {
                if (!category.isNullOrEmpty() && !categoryTitle.isNullOrEmpty()) {
                    CommonUtils.startStockByCategoryActivity(
                        requireContext(),
                        category, categoryTitle
                    )
                } else {
                    if (dataItem != null) {
                        CommonUtils.startDescriptionActivity(
                            requireContext(),
                            dataItem
                        )
                    }
                }
                return
            }
            mInterstitialAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        if (!category.isNullOrEmpty() && !categoryTitle.isNullOrEmpty()) {
                            Constant.moreLoadAd = 1
                            category.let {
                                CommonUtils.startStockByCategoryActivity(
                                    requireContext(),
                                    it, categoryTitle
                                )
                            }
                        } else {
                            Constant.descriptionLoadAd = 1
                            if (dataItem != null) {
                                CommonUtils.startDescriptionActivity(
                                    requireContext(),
                                    dataItem
                                )
                            }
                        }
                        initInterstitialAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(error: AdError) {
                        super.onAdFailedToShowFullScreenContent(error)
                        if (category != null) {
                            categoryTitle?.let {
                                CommonUtils.startStockByCategoryActivity(
                                    requireContext(),
                                    category, it
                                )
                            }
                        } else {
                            dataItem?.let {
                                CommonUtils.startDescriptionActivity(
                                    requireContext(),
                                    it
                                )
                            }
                        }
                    }
                }
        } else {
            moveTo(category, categoryTitle, dataItem)
        }


    }


    private fun moveTo(
        category: String? = null,
        categoryTitle: String? = null,
        dataItem: DataItem? = null,
    ) {
        if (dataItem != null) {
            CommonUtils.startDescriptionActivity(
                requireContext(),
                dataItem
            )
        } else {
            CommonUtils.startStockByCategoryActivity(
                requireContext(),
                category.toString(), categoryTitle.toString()
            )
        }
    }

    private fun initApi() {
        binding.progressCircular.visibility=ProgressBar.VISIBLE
        homeViewModel.getStockData(Constant.homeApi)
        lifecycleScope.launchWhenCreated {
            homeViewModel.stockData.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        Constant.showAd_description = false
                        Constant.stockByCategory_showAd = false
                        binding.noDataFound.visibility = View.GONE
                        if (it.data != null) {
                            if (it.data.versionCode == BuildConfig.VERSION_CODE && it.data.showAd) {
                                Constant.showAd_description = it.data.showAd
                                Constant.stockByCategory_showAd = it.data.showAd
                            } else if (it.data.versionCode != BuildConfig.VERSION_CODE) {
                                Constant.showAd_description = true
                                Constant.stockByCategory_showAd = true
                            }
                            if (it.data.smallcap.isNullOrEmpty() && it.data.midcap.isNullOrEmpty() && it.data.bluechip.isNullOrEmpty() && it.data.data.isNullOrEmpty()) {
                                binding.progressCircular.visibility = ProgressBar.GONE
                            } else {
                                if (binding.swipeToRefresh.isRefreshing){
                                    binding.swipeToRefresh.isRefreshing = false
                                    binding.progressCircular.visibility = ProgressBar.GONE
                                }
                            }
                            initData()
                        } else {
                            if (PreferenceUtil.booleanPreference(Constant.dataBase)
                                    ?.get() == false
                            ) {
                                binding.noDataFound.visibility = View.VISIBLE
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        binding.swipeToRefresh.isRefreshing = false
                        binding.progressCircular.visibility = ProgressBar.GONE
                        if (PreferenceUtil.booleanPreference(Constant.dataBase)?.get() == false) {
                            binding.noDataFound.visibility = View.VISIBLE
                        }
                    }
                    is NetworkResult.Loading -> {
                        binding.progressCircular.visibility = ProgressBar.VISIBLE
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun initAdapter() {

        initMidAdapter()
        initBlueChipAdapter()
        initSmallAdapter()
    }

    private fun initData() {
        homeViewModel.getStockDataFromRoom()
        homeViewModel.getStockDataFromRoom.observe(requireActivity()) { data ->
            if (data != null) {
                binding.noDataFound.visibility = View.GONE
                binding.mainLiner.visibility = View.VISIBLE
                if (data.data != null && data.data.isNotEmpty()) {
                    binding.loutCarousel.visibility = View.VISIBLE
                    carouselAdapter.upDateList(data.data)
                }

                if (data.bluechip != null && data.bluechip.isNotEmpty()) {
                    binding.loutBlueChip.visibility = View.VISIBLE
                    blueChipAdapter.upDateList(data.bluechip)
                }

                if (data.midcap != null && data.midcap.isNotEmpty()) {
                    binding.loutMidCap.visibility = View.VISIBLE
                    midCapAdapter.upDateList(data.midcap)
                }

                if (data.smallcap != null && data.smallcap.isNotEmpty()) {
                    binding.loutSmallCap.visibility = View.VISIBLE
                    smallCapAdapter.upDateList(data.smallcap)
                }
                binding.progressCircular.visibility = ProgressBar.GONE
            } else {
                binding.mainLiner.visibility = View.GONE
                binding.noDataFound.visibility = View.VISIBLE
                binding.progressCircular.visibility = ProgressBar.GONE
            }
        }
        binding.swipeToRefresh.isRefreshing = false
        binding.mainLiner.visibility = View.VISIBLE
    }

    private fun initSmallAdapter() {
        binding.rcvSmallCap.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        binding.rcvSmallCap.adapter = smallCapAdapter

        smallCapAdapter.onItemClick = {
            isFromMore = false
            showInterstitialAd(dataItem = it)
        }
    }

    private fun initBlueChipAdapter() {
        binding.rcvBlueChip.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        binding.rcvBlueChip.adapter = blueChipAdapter

        blueChipAdapter.onItemClick = {
            isFromMore = false
            showInterstitialAd(dataItem = it)

        }
    }

    private fun initMidAdapter() {
        binding.rcvMidCap.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        binding.rcvMidCap.adapter = midCapAdapter

        midCapAdapter.onItemClick = {
            isFromMore = false
            showInterstitialAd(dataItem = it)
        }
    }

    private fun initCarouselView() {
        binding.carouselRecyclerview.adapter = carouselAdapter
        binding.carouselRecyclerview.apply {
            set3DItem(true)
            setAlpha(true)
            setInfinite(true)
        }

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.carouselRecyclerview)
        binding.indicator.attachToRecyclerView(binding.carouselRecyclerview, pagerSnapHelper)
        carouselAdapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)

        carouselAdapter.onItemClick = {
            isFromMore = false
            showInterstitialAd(dataItem = it)
        }

    }

    companion object {
        fun newInstance() = HomeFragment()
    }


}