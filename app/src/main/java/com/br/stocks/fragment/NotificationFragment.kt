package com.br.stocks.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.br.stocks.R
import com.br.stocks.adapter.NotificationAdapter
import com.br.stocks.databinding.FragmentNotificationBinding
import com.br.stocks.models.NotificationData
import com.br.stocks.ui.DescriptionActivity
import com.br.stocks.utils.CommonUtils
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.isInternetConnected
import com.br.stocks.viewmodel.NotificationViewModel
import com.google.android.gms.ads.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        notificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        notificationAdapter = NotificationAdapter()

        if (isInternetConnected(requireContext())) {
            binding.noDataFound.visibility = View.GONE
            callApi()
        } else {
            binding.noDataFound.visibility = View.VISIBLE
            binding.notificationList.visibility = View.GONE
            requireContext().toast(getString(R.string.no_internet))
        }
        if (Constant.showAd_description && Constant.stockByCategory_showAd) {
            initAd()
        }
        return binding.root
    }

    private fun callApi() {

        notificationViewModel.getNotification(Constant.notification)
        lifecycle.coroutineScope.launch {
            notificationViewModel.notification.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data?.data != null && it.data.data.isNotEmpty()) {
                            notificationAdapter.setNotificationItem(it.data.data as ArrayList<NotificationData>)
                            notificationAdapter.onNotificationItemClick={notification->
                                val i=Intent(requireActivity(),DescriptionActivity::class.java)
                                i.putExtra("post_id","${notification.post_id}")
                                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(i)
                            }
                            binding.notificationList.apply {
                                binding.notificationList.adapter = notificationAdapter
                            }
                            binding.progressCircular.visibility = ProgressBar.GONE
                            binding.notificationList.visibility = View.VISIBLE
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

    private fun initAd() {
        MobileAds.initialize(requireContext()) {}
        val mAdView = AdView(requireContext())
        mAdView.adSize = AdSize.BANNER
        mAdView.adUnitId = CommonUtils.getBannerAdsKey()
        binding.notificationAdView.addView(mAdView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                binding.notificationAdView.visibility = View.VISIBLE
            }

            override fun onAdOpened() {
            }

            override fun onAdClicked() {
            }

            override fun onAdClosed() {

            }
        }
    }

    companion object {
        fun newInstance() = NotificationFragment()
    }
}