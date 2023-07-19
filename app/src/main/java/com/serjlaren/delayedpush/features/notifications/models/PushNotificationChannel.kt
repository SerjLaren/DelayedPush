package com.serjlaren.delayedpush.features.notifications.models

import com.serjlaren.delayedpush.R
import com.serjlaren.delayedpush.common.AppConstants

sealed class PushNotificationChannel(val channelId: String, val channelName: Int, val channelDescription: Int) {
    object TimerAlarm : PushNotificationChannel(
        AppConstants.NOTIFICATION_CHANNEL_ID_TIMER_ALARM,
        R.string.ntf_channel_timer_alarm_name,
        R.string.ntf_channel_timer_alarm_description
    )
}