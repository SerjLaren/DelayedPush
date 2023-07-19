package com.serjlaren.delayedpush.features.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.serjlaren.delayedpush.R
import com.serjlaren.delayedpush.features.notifications.models.PushNotification
import com.serjlaren.delayedpush.features.notifications.models.PushNotificationChannel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushNotificationsManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(PushNotificationChannel.TimerAlarm)
        }
    }

    fun showNotification(appNotification: PushNotification) {
        val builder = NotificationCompat.Builder(
            context,
            appNotification.notificationChannelId
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setStyle(NotificationCompat.BigTextStyle().bigText(appNotification.description))
            .setContentTitle(appNotification.title)
            .setContentText(appNotification.description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(appNotification.pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(
                    appNotification.notificationId,
                    builder.build()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(appNotificationChannel: PushNotificationChannel) {
        val channel = NotificationChannel(
            appNotificationChannel.channelId,
            context.getString(appNotificationChannel.channelName),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(appNotificationChannel.channelDescription)
        }
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )
    }
}