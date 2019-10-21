package com.kubsu.timetable.utils

import android.content.Context
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.timetable.utils.ui.materialAlert
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
        activity.materialAlert(
            message = getString(messageForUser),
            positiveButtonText = android.R.string.yes,
            onOkButtonClick = okButton,
            onNoButtonClick = noButton,
            windowFeature = Window.FEATURE_NO_TITLE
        )
    }
}