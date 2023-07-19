package com.serjlaren.delayedpush.features.timer.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serjlaren.delayedpush.R
import com.serjlaren.delayedpush.common.AppConstants
import com.serjlaren.delayedpush.features.providers.ResourcesProvider
import com.serjlaren.delayedpush.features.timer.TimerManager
import com.serjlaren.delayedpush.features.timer.screen.models.TimerFragmentAction
import com.serjlaren.delayedpush.features.timer.screen.models.TimerFragmentViewEvent
import com.serjlaren.delayedpush.features.timer.screen.models.TimerFragmentViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerFragmentViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val timerManager: TimerManager,
) : ViewModel() {

    private val _viewState = MutableStateFlow<TimerFragmentViewState>(TimerFragmentViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _action = MutableSharedFlow<TimerFragmentAction>()
    val action = _action.asSharedFlow()

    fun initViewModel() {
        _viewState.update {
            TimerFragmentViewState.Content(
                additionalInfoText = resourcesProvider.getString(R.string.scr_timer_txt_additional_info),
                settingsButtonText = resourcesProvider.getString(R.string.scr_timer_btn_settings),
                timerButtonText = resourcesProvider.getString(R.string.scr_timer_btn_timer),
            )
        }
    }

    fun obtainEvent(viewEvent: TimerFragmentViewEvent) {
        when (viewEvent) {
            TimerFragmentViewEvent.SettingsButtonClicked -> {
                viewModelScope.launch {
                    _action.emit(TimerFragmentAction.NavigateToSettings)
                }
            }
            TimerFragmentViewEvent.TimerButtonClicked -> {
                viewModelScope.launch {
                    timerManager.startTimerAlarmReceiverBroadcast(AppConstants.TIMER_ALARM_MILLIS)
                    _action.emit(TimerFragmentAction.NotifyTimerStarted)
                }
            }
        }
    }
}