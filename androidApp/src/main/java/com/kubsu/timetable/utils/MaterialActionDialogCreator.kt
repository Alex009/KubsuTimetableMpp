package com.kubsu.timetable.utils

import android.content.Context
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thelittlefireman.appkillermanager.Failure
import com.thelittlefireman.appkillermanager.devices.DeviceBase
import com.thelittlefireman.appkillermanager.ui.ActionDialogCreator

class MaterialActionDialogCreator(
    device: DeviceBase,
    private val activity: AppCompatActivity,
    onFailure: (Failure) -> Unit
) : ActionDialogCreator(device, activity, onFailure) {
    override fun Context.showDialog(
        messageForUser: Int,
        okButton: () -> Unit,
        noButton: () -> Unit
    ) {
        val alert = materialAlert(
            message = messageForUser,
            positiveButtonText = android.R.string.yes,
            onOkButtonClick = okButton,
            onNoButtonClick = noButton,
            windowFeature = Window.FEATURE_NO_TITLE,
            cancelable = false
        )
        alert.show()
        activity
            .lifecycle
            .addObserver(
                LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_DESTROY)
                        alert.dismiss()
                }
            )
    }

    private fun Context.materialAlert(
        message: Int,
        title: Int? = null,
        positiveButtonText: Int = android.R.string.ok,
        negativeButtonText: Int = android.R.string.cancel,
        onOkButtonClick: (() -> Unit)?,
        onNoButtonClick: (() -> Unit)? = null,
        windowFeature: Int? = null,
        cancelable: Boolean,
        themeRes: Int = 0
    ): AlertDialog =
        MaterialAlertDialogBuilder(this, themeRes)
            .also { alert ->
                alert.setMessage(message)
                title?.let(alert::setTitle)

                onOkButtonClick?.let {
                    alert.setPositiveButton(positiveButtonText) { _, _ ->
                        it.invoke()
                    }
                }
                onNoButtonClick?.let {
                    alert.setNegativeButton(negativeButtonText) { _, _ ->
                        it.invoke()
                    }
                }
                alert.setCancelable(cancelable)
            }
            .create()
            .also {
                windowFeature?.let(it::requestWindowFeature)
            }
}