package com.serjlaren.delayedpush.features.providers

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getString(@StringRes strResId: Int): String {
        return context.getString(strResId)
    }
}