package com.serjlaren.delayedpush.features.timer.screen.models

sealed class TimerFragmentViewEvent {
    object SettingsButtonClicked : TimerFragmentViewEvent()
    object TimerButtonClicked : TimerFragmentViewEvent()
}