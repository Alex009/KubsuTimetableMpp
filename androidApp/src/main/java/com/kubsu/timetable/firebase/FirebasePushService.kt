package com.kubsu.timetable.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kubsu.timetable.R
import com.kubsu.timetable.base.AppActivity
import com.kubsu.timetable.utils.Logger

class FirebasePushService : FirebaseMessagingService(), Logger {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.forEach {
            info("remote message data: ${it.key} to ${it.value}")
        }
        remoteMessage.notification?.let {
            info("remote message notification: ${it.title}, ${it.body}")
        }

        showNotification(remoteMessage.data["id"] ?: "NOT CONTAINS MESSAGE")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        info("new token: $token")
    }

    private fun showNotification(message: String) {
        val intent = Intent(this, AppActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Title")
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setVisibility(VISIBILITY_PUBLIC)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            val manager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        } else {
            notificationBuilder.setVibrate(longArrayOf(100, 100, 100, 100, 0))
        }

        NotificationManagerCompat
            .from(this)
            .notify(
                message.hashCode(),
                notificationBuilder.build()
            )
    }
}