package com.serjlaren.delayedpush.features.timer.screen

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serjlaren.delayedpush.R
import com.serjlaren.delayedpush.common.extensions.launchWhenStarted
import com.serjlaren.delayedpush.databinding.FragmentTimerBinding
import com.serjlaren.delayedpush.features.timer.screen.models.TimerFragmentAction
import com.serjlaren.delayedpush.features.timer.screen.models.TimerFragmentViewEvent
import com.serjlaren.delayedpush.features.timer.screen.models.TimerFragmentViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TimerFragment : Fragment(R.layout.fragment_timer) {

    private val viewModel: TimerFragmentViewModel by viewModels()
    private val viewBinding: FragmentTimerBinding by viewBinding()

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
            } else {
                viewModel.obtainEvent(TimerFragmentViewEvent.TimerButtonClicked)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
        viewModel.initViewModel()
    }

    private fun initViews() {
        with(viewBinding) {
            settingsButton.setOnClickListener { viewModel.obtainEvent(TimerFragmentViewEvent.SettingsButtonClicked) }
            timerButton.setOnClickListener { timerButtonClicked() }
        }
    }

    private fun bindViewModel() {
        with(viewModel) {
            viewState.onEach { viewState ->
                processViewState(viewState)
            }.launchWhenStarted(viewLifecycleOwner, lifecycleScope)

            action.onEach { action ->
                processAction(action)
            }.launchWhenStarted(viewLifecycleOwner, lifecycleScope)
        }
    }

    private fun processViewState(viewState: TimerFragmentViewState) {
        when (viewState) {
            is TimerFragmentViewState.Content -> {
                with(viewBinding) {
                    additionalInfoTextView.text = viewState.additionalInfoText
                    settingsButton.text = viewState.settingsButtonText
                    timerButton.text = viewState.timerButtonText
                }
            }

            TimerFragmentViewState.Error -> {}
            TimerFragmentViewState.Loading -> {}
        }
    }

    private fun processAction(action: TimerFragmentAction) {
        when (action) {
            TimerFragmentAction.NavigateToSettings -> {
                startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
            TimerFragmentAction.NotifyTimerStarted -> {
                Toast.makeText(requireContext(), getString(R.string.scr_timer_txt_timer_started), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun timerButtonClicked() {
        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.obtainEvent(TimerFragmentViewEvent.TimerButtonClicked)
        }
    }

    private fun showSettingDialog() {
        AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("")
            .setMessage(getString(R.string.scr_any_txt_settings_to_permission))
            .setPositiveButton(getString(R.string.scr_any_txt_ok)) { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${requireContext().packageName}")
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.scr_any_txt_cancel), null)
            .create()
            .show()
    }

    private fun showNotificationPermissionRationale() {
        AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("")
            .setMessage(getString(R.string.scr_any_notifications_permission_required))
            .setPositiveButton(getString(R.string.scr_any_txt_ok)) { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton(getString(R.string.scr_any_txt_cancel), null)
            .create()
            .show()
    }
}