package com.serjlaren.delayedpush.features.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.serjlaren.delayedpush.features.notifications.PushNotificationsManager
import com.serjlaren.delayedpush.features.notifications.models.PushNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimerAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var pushNotificationsManager: PushNotificationsManager

    override fun onReceive(context: Context, intent: Intent?) {
        pushNotificationsManager.showNotification(
            PushNotification.TimerAlarm(context)
        )
    }
}