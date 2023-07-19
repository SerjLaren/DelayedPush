package com.serjlaren.delayedpush.features.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.serjlaren.delayedpush.common.AppConstants
import com.serjlaren.delayedpush.common.extensions.api31PendingIntentFlag
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun startTimerAlarmReceiverBroadcast(afterMillis: Long) {
        cancelTimerAlarmReceiverBroadcast()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + afterMillis,
            generateTimerAlarmReceiverBroadcast()
        )
    }

    private fun cancelTimerAlarmReceiverBroadcast() {
        alarmManager.cancel(
            generateTimerAlarmReceiverBroadcast()
        )
    }

    private fun generateTimerAlarmReceiverBroadcast(): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            AppConstants.ALARM_TIMER_REQUEST_CODE,
            Intent(
                context,
                TimerAlarmReceiver::class.java
            ),
            PendingIntent.FLAG_UPDATE_CURRENT.api31PendingIntentFlag()
        )
}