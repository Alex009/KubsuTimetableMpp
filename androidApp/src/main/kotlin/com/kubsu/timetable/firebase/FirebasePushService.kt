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
import com.kubsu.timetable.base.App
import com.kubsu.timetable.base.AppActivity
import com.kubsu.timetable.data.mapper.diff.DataDiffDtoMapper
import com.kubsu.timetable.data.network.dto.diff.DataDiffNetworkDto
import com.kubsu.timetable.data.storage.user.token.UndeliveredToken
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import com.kubsu.timetable.extensions.isMustDisplayInNotification
import com.kubsu.timetable.extensions.toJson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

class FirebasePushService : FirebaseMessagingService(), KodeinAware {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val dataDiffModel = json.fromJson(
            DataDiffNetworkDto.serializer(),
            remoteMessage.data.toJson(json)
        )
        val dataDiffEntity = DataDiffDtoMapper.toEntity(dataDiffModel)
        GlobalScope.launch {
            mixinInteractor.registerDataDiff(dataDiffEntity)
            if (
                userInteractor.getCurrentUserOrNull()?.id == dataDiffEntity.userId
                && isMustDisplayInNotification(dataDiffEntity)
            )
                showNotification(notificationId = dataDiffEntity.hashCode())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        GlobalScope.launch {
            userInteractor.newToken(UndeliveredToken(token))
        }
    }

    private fun showNotification(notificationId: Int) {
        val intent = Intent(this, AppActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(getString(R.string.check_new_timetable))
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
                notificationId,
                notificationBuilder.build()
            )
    }

    override val kodein: Kodein by lazy { (application as App).kodein }

    private val userInteractor: UserInteractor
        get() {
            val userInteractor by kodein.instance<UserInteractor>()
            return userInteractor
        }

    private val mixinInteractor: SyncMixinInteractor
        get() {
            val mixinInteractor by kodein.instance<SyncMixinInteractor>()
            return mixinInteractor
        }

    private val json: Json
        get() {
            val json by kodein.instance<Json>()
            return json
        }
}