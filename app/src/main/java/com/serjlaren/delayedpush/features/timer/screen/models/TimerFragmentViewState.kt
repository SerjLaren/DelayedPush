package com.serjlaren.delayedpush.features.timer.screen.models

sealed class TimerFragmentViewState {
    object Loading : TimerFragmentViewState()
    data class Content(
        val additionalInfoText: String,
        val settingsButtonText: String,
        val timerButtonText: String,
    ) : TimerFragmentViewState()
    object Error : TimerFragmentViewState()
}