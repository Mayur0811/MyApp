package com.br.stocks.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.br.stocks.R
import com.br.stocks.databinding.LayoutAdBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class AdViewHolder(itemView: View, var activity: Activity) : RecyclerView.ViewHolder(itemView) {

    var binding: LayoutAdBinding = LayoutAdBinding.bind(itemView)

    @SuppressLint("InflateParams")
    fun bindAdData() {
        val builder = AdLoader.Builder(activity, CommonUtils.getNativeAdKey())
            .forNativeAd { nativeAd ->
                val nativeAdView = activity.layoutInflater
                    .inflate(R.layout.gnt_custom_small_template_view, null) as NativeAdView
                populateNativeADView(nativeAd, nativeAdView)
                binding.adLayout.removeAllViews()
                binding.adLayout.addView(nativeAdView)
            }
        if (Constant.showAd_description && Constant.stockByCategory_showAd) {
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                }
            }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    private fun populateNativeADView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            (adView.iconView as AppCompatImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView?.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating =
                nativeAd.starRating?.toDouble()!!.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
    }

}

