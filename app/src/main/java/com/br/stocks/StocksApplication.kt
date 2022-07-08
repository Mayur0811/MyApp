package com.br.stocks

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.br.stocks.utils.CommonsCore
import com.br.stocks.utils.Constant
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StocksApplication : Application(), LifecycleObserver {

    companion object {
        var isAppInBackground = false
    }


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        CommonsCore.init(this)
        Constant.ENABLE_LOGGING = BuildConfig.DEBUG
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

    }

    @Override
    override fun onLowMemory() {
        super.onLowMemory()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        isAppInBackground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        isAppInBackground = false
    }
}