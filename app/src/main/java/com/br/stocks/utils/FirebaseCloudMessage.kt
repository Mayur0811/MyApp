package com.br.stocks.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.br.stocks.R
import com.br.stocks.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseCloudMessage : FirebaseMessagingService() {

    var message: String? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(ContentValues.TAG, "onNewToken: $token")
        PreferenceUtil.stringPreference(Constant.deviceToken).set(token)
    }

    override fun onMessageReceived(rm: RemoteMessage) {
        super.onMessageReceived(rm)
        rm.notification?.let {
            showNotification(it.title, it.body,rm.data)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag", "WrongConstant")
    private fun showNotification(title: String?, body: String?,map:Map<String,String>) {

        val intent = Intent(applicationContext, MainActivity::class.java)
        if (map.containsKey("post_id"))
        {
            intent.putExtra("post_id", map["post_id"])
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;

        val pendingIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 100, intent, FLAG_UPDATE_CURRENT)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannelForAndroidO()
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val pattern = longArrayOf(500, 500, 500, 500, 500)
        val notificationBuilder = NotificationCompat.Builder(baseContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setContentTitle(title)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setLights(Color.BLUE, 1, 1)
            .setVibrate(pattern)
            .setContentIntent(pendingIntent)

        notificationManager.notify((0..100000).random(), notificationBuilder.build())
    }

    private val NOTIFICATION_CHANNEL_ID = "trading_tip_channel"

    private fun createNotificationChannelForAndroidO() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Trading Tip.",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "Trading Tip Notification"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
