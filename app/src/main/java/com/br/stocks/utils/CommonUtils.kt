package com.br.stocks.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.br.stocks.BuildConfig
import com.br.stocks.R
import com.br.stocks.models.DataItem
import com.br.stocks.ui.DescriptionActivity
import com.br.stocks.ui.StocksByCategoryActivity
import com.google.gson.Gson
import java.util.*


object CommonUtils {

    private lateinit var pBarDialog: ProgressBarDialog
    private lateinit var noDataDialog: NoDataFoundDialog

    fun getDateRange(): Pair<Date, Date> {
        var beginning: Date
        var end: Date
        run {
            val calendar: Calendar = getCalendarForNow()
            calendar.set(
                Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
            )
            setTimeToBeginningOfDay(calendar)
            beginning = calendar.time
        }
        run {
            val calendar: Calendar = getCalendarForNow()
            calendar.add(Calendar.MONTH, +1)
            calendar.set(
                Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            setTimeToEndofDay(calendar)
            end = calendar.time
        }
        return Pair(beginning, end)
    }

    private fun getCalendarForNow(): Calendar {
        val calendar: Calendar = GregorianCalendar.getInstance()
        calendar.time = Date()
        return calendar
    }

    private fun setTimeToBeginningOfDay(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    private fun setTimeToEndofDay(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
    }

    fun getDate(date: Date): String {
        val day = DateFormat.format("dd", date)
        val month = DateFormat.format("MM", date)
        val year = DateFormat.format("yyyy", date)
        return StringBuilder().append(year).append(month).append(day).toString()
    }

    fun getMonth(stringDate: String): Int {

        return when {
            stringDate.contains("Jan") -> {
                0
            }
            stringDate.contains("Feb") -> {
                1
            }
            stringDate.contains("Mar") -> {
                2
            }
            stringDate.contains("Apr") -> {
                3
            }
            stringDate.contains("May") -> {
                4
            }
            stringDate.contains("Jun") -> {
                5
            }
            stringDate.contains("Jul") -> {
                6
            }
            stringDate.contains("Aug") -> {
                7
            }
            stringDate.contains("Sep") -> {
                8
            }
            stringDate.contains("Oct") -> {
                9
            }
            stringDate.contains("Nov") -> {
                10
            }
            stringDate.contains("Dec") -> {
                11
            }
            else -> {
                Log.e("TAG", "getMonth: $stringDate")
            }
        }

    }

    fun showProgressBar(ctx: Context) {
        try {
            pBarDialog = ProgressBarDialog(ctx)
            pBarDialog.setCancelable(false)
            pBarDialog.setCanceledOnTouchOutside(false)
            pBarDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissProgressBar() {
        try {
            if (this::pBarDialog.isInitialized) {
                if (pBarDialog.isShowing)
                    pBarDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun Context.toast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun getBannerAdsKey(): String =
        if (BuildConfig.DEBUG) Constant.bannerAd else Constant.LiveBannerAd

    fun getInterstitialAdsKey(): String =
        if (BuildConfig.DEBUG) Constant.interstitialAd else Constant.LiveInterstitialAd

    fun getNativeAdKey(): String =
        if (BuildConfig.DEBUG) Constant.nativeAdId else Constant.LiveNativeAd

    fun startDescriptionActivity(ctx: Context, data: DataItem) {
        val i = Intent(ctx, DescriptionActivity::class.java)
        i.putExtra("data", Gson().toJson(data))
        ctx.startActivity(i)
    }

    fun startStockByCategoryActivity(ctx: Context, category: String, categoryName: String) {
        val i = Intent(ctx, StocksByCategoryActivity::class.java)
        i.putExtra(ctx.getString(R.string.category), category)
        i.putExtra(
            ctx.getString(R.string.category_name),
            categoryName
        )
        ctx.startActivity(i)
    }

    fun showNoDataFound(ctx: Context) {
        try {
            noDataDialog = NoDataFoundDialog(ctx)
            noDataDialog.setCancelable(false)
            noDataDialog.setCanceledOnTouchOutside(true)
            noDataDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissNoDataFound() {
        try {
            if (this::noDataDialog.isInitialized) {
                if (noDataDialog.isShowing)
                    noDataDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun isInternetConnected(context: Context): Boolean {
    var isConnected = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network: Network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(network)!!

        isConnected = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            activeNetworkInfo?.run {
                isConnected = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return isConnected
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}