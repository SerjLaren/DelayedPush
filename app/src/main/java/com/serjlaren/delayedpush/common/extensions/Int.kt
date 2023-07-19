package com.serjlaren.delayedpush.common.extensions

import android.app.PendingIntent
import android.os.Build

fun Int.api31PendingIntentFlag() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        this or PendingIntent.FLAG_MUTABLE
    else
        this