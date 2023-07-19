package com.serjlaren.delayedpush.features.timer.screen.models

sealed class TimerFragmentAction {
    object NavigateToSettings : TimerFragmentAction()
    object NotifyTimerStarted : TimerFragmentAction()
}