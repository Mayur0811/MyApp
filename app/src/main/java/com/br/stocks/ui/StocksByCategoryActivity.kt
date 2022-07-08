package com.br.stocks.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.stocks.R
import com.br.stocks.adapter.StockByCategoryAdapter
import com.br.stocks.databinding.ActivityStocksByCategoryBinding
import com.br.stocks.models.DataItem
import com.br.stocks.utils.*
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.viewmodel.StockByCategoryViewModel
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StocksByCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStocksByCategoryBinding
   private lateinit var stockByCategoryViewModel: StockByCategoryViewModel
    private var adapter = StockByCategoryAdapter()
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stocks_by_category)
        stockByCategoryViewModel = ViewModelProvider(this)[StockByCategoryViewModel::class.java]

        initInterstitialAd()
        initView()
        binding.txtCompanyName.text = intent.getStringExtra(getString(R.string.category_name))
        if (isInternetConnected(this)) {
            binding.noDataFound.visibility = View.GONE
            initApi()
        } else {
            this.toast(getString(R.string.no_internet))
            binding.noDataFound.visibility = View.VISIBLE
            binding.rcvStockByCategory.visibility = View.GONE
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun initView() {
        if (Constant.showAd_description && Constant.stockByCategory_showAd) {
            initAd()
        }
        initAdapter()
    }

    private fun initApi() {
        val category = intent.getStringExtra(getString(R.string.category))
        val url = StringBuilder().append(Constant.stockByCategory).append(category).toString()

        stockByCategoryViewModel.getStockByCategory(url)
        lifecycleScope.launchWhenCreated {
            stockByCategoryViewModel.stockByCategoryResponse.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            if (it.data.data != null) {
                                adapter.upDateList(it.data.data)
                            }
                            binding.progressCircular.visibility = ProgressBar.GONE
                            binding.rcvStockByCategory.visibility = View.VISIBLE
                        } else {
                            binding.progressCircular.visibility = ProgressBar.GONE
                            binding.noDataFound.visibility = View.VISIBLE
                        }
                    }
                    is NetworkResult.Error -> {
                        binding.progressCircular.visibility = ProgressBar.GONE
                        binding.noDataFound.visibility = View.VISIBLE
                    }
                    is NetworkResult.Loading -> {
                        binding.progressCircular.visibility = ProgressBar.VISIBLE

                    }
                    else -> {}
                }
            }
        }
    }

    private fun initAdapter() {
        binding.rcvStockByCategory.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rcvStockByCategory.adapter = adapter

        adapter.onItemClick = {
            showInterstitialAd(it)
        }
    }

    private fun showInterstitialAd(data: DataItem) {
        if (mInterstitialAd != null) {
            if (Constant.showAd_description && Constant.descriptionLoadAd == 0) {
                mInterstitialAd?.show(this@StocksByCategoryActivity)
            } else {
                CommonUtils.startDescriptionActivity(
                    this@StocksByCategoryActivity,
                    data
                )
                return
            }
            mInterstitialAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Constant.descriptionLoadAd = 1
                        CommonUtils.startDescriptionActivity(
                            this@StocksByCategoryActivity,
                            data
                        )
                    }

                    override fun onAdFailedToShowFullScreenContent(error: AdError) {
                        super.onAdFailedToShowFullScreenContent(error)
                        CommonUtils.startDescriptionActivity(
                            this@StocksByCategoryActivity,
                            data
                        )
                    }
                }
        } else {
            CommonUtils.startDescriptionActivity(
                this@StocksByCategoryActivity,
                data
            )
        }
    }

    private fun initInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,
            CommonUtils.getInterstitialAdsKey(),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })

    }

    private fun initAd() {
        MobileAds.initialize(this)
        val mAdView = AdView(this)
        mAdView.adSize = AdSize.BANNER
        mAdView.adUnitId = CommonUtils.getBannerAdsKey()
        binding.adView.addView(mAdView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        binding.adView.visibility = View.VISIBLE
        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                binding.adView.visibility = View.GONE
            }

            override fun onAdOpened() {
                binding.adView.visibility = View.VISIBLE
            }

            override fun onAdClicked() {
            }

            override fun onAdClosed() {
            }
        }
    }
}