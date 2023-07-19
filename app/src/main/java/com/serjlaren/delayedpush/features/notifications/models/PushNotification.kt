package com.serjlaren.delayedpush.features.notifications.models

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.serjlaren.delayedpush.MainActivity
import com.serjlaren.delayedpush.R
import com.serjlaren.delayedpush.common.AppConstants
import com.serjlaren.delayedpush.common.extensions.api31PendingIntentFlag

sealed class PushNotification(
    val notificationId: Int,
    val notificationChannelId: String,
    val title: String,
    val description: String,
    val pendingIntent: PendingIntent,
) {
    class TimerAlarm(context: Context) :
        PushNotification(
            notificationId = PushNotificationType.TimerAlarm.notificationId,
            notificationChannelId = PushNotificationChannel.TimerAlarm.channelId,
            title = context.getString(R.string.ntf_txt_timer_title),
            description = context.getString(R.string.ntf_txt_timer_description),
            pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                },
                PendingIntent.FLAG_UPDATE_CURRENT.api31PendingIntentFlag()
            )
        )
}

enum class PushNotificationType(val notificationId: Int) {
    TimerAlarm(AppConstants.NOTIFICATION_ID_TIMER_ALARM),
}