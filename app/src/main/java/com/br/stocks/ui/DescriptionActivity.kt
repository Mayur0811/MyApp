package com.br.stocks.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.br.stocks.R
import com.br.stocks.databinding.ActivityDescriptionBinding
import com.br.stocks.models.DataItem
import com.br.stocks.utils.CommonUtils
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class DescriptionActivity : AppCompatActivity() {

    var inputFormat= SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    var outputFormat= SimpleDateFormat("dd MMM, yyyy",Locale.ENGLISH)

    private lateinit var binding: ActivityDescriptionBinding
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_description)
        homeViewModel=ViewModelProvider(this)[HomeViewModel::class.java]
        binding.back.setOnClickListener {
            finish()
        }
        if (Constant.showAd_description && Constant.stockByCategory_showAd) {
            initAd()
        }
        initView()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initView()
    }

    private fun initView() {
        if (intent.getStringExtra("post_id") != null) {
            val postId = intent.getStringExtra("post_id")
            intent.removeExtra("post_id")
            binding.descriptionProgressBar.visibility = ProgressBar.VISIBLE
            if (isInternetConnected(this)) {
                postId?.let { homeViewModel.getPostData(it) }
                lifecycleScope.launchWhenCreated {
                    homeViewModel.getPostData.collect{
                        when(it)
                        {
                            is NetworkResult.Success->{
                                val data:DataItem = it.data!!.data

                                Glide.with(this@DescriptionActivity).load(Constant.imgApi+data.image).into(binding.imageCompany)
                                binding.txtCompanyName.text = data.stockName
                                binding.txtDescription.text = data.description
                                binding.txtStockType.text = data.companyType
                                binding.txtShareType.text = data.shareType
                                binding.txtTradeType.text = data.tradeType
                                binding.txtStopLoss.text = data.stopLoss.toString()
                                binding.txtTarget.text = data.target
                                binding.txtSymbol.text = data.symbol

                                val date = data.date?.let { inputFormat.parse(it) }
                                binding.txtDate.text = date?.let { outputFormat.format(it) }

                                binding.imageCompany.setOnClickListener {
                                    val i = Intent(this@DescriptionActivity, ImageViewerActivity::class.java)
                                    i.putExtra("image", Constant.imgApi+data.image)
                                    startActivity(i)
                                }
                                binding.descriptionProgressBar.visibility = ProgressBar.GONE
                            }
                            is NetworkResult.Error -> {
                                Log.e("msg", "${it.message}")
                                binding.descriptionProgressBar.visibility = ProgressBar.GONE
                            }
                            is NetworkResult.Loading -> {
                                binding.descriptionProgressBar.visibility = ProgressBar.VISIBLE
                            }
                            else -> {
                                binding.descriptionProgressBar.visibility = ProgressBar.GONE
                            }
                        }
                    }
                }
            } else {
                this.toast(getString(R.string.no_internet))
                binding.descriptionProgressBar.visibility = ProgressBar.GONE
            }
        } else {
            val string = intent.getStringExtra("data")
            val data = Gson().fromJson(string, DataItem::class.java)

            if (data != null) {
                Glide.with(this).load(data.image).into(binding.imageCompany)
                binding.txtCompanyName.text = data.stockName
                binding.txtDescription.text = data.description
                binding.txtStockType.text = data.companyType
                binding.txtShareType.text = data.shareType
                binding.txtTradeType.text = data.tradeType
                binding.txtStopLoss.text = data.stopLoss.toString()
                binding.txtTarget.text = data.target
                binding.txtSymbol.text = data.symbol

                val date = data.date?.let { inputFormat.parse(it) }
                binding.txtDate.text = date?.let { outputFormat.format(it) }

                binding.imageCompany.setOnClickListener {
                    val i = Intent(this, ImageViewerActivity::class.java)
                    i.putExtra("image", data.image)
                    startActivity(i)
                }

            }
        }


    }

    private fun initAd() {
        MobileAds.initialize(this)
        val mAdView = AdView(this)
        mAdView.adSize = AdSize.BANNER
        mAdView.adUnitId = CommonUtils.getBannerAdsKey()
        binding.adView.addView(mAdView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                binding.adView.visibility = View.VISIBLE
            }

            override fun onAdOpened() {
            }

            override fun onAdClicked() {
            }

            override fun onAdClosed() {
            }
        }
    }

}