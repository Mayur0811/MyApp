package com.br.stocks.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable


class NotificationService : Service() {

    private fun startNotificationListener() {
        //start's a new thread
        Thread { //fetching notifications from server
            //if there is notifications then call this method
            try {
                startService(Intent(this, FireBaseCloudMessage::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun onCreate() {
        startNotificationListener()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}