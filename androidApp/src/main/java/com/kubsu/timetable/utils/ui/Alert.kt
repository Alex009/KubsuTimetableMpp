package com.kubsu.timetable.utils.ui

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kubsu.timetable.utils.addObserver
import com.kubsu.timetable.utils.removeObserver

fun ComponentActivity.materialAlert(
    message: String,
    title: String? = null,
    positiveButtonText: Int = android.R.string.ok,
    negativeButtonText: Int = android.R.string.cancel,
    onOkButtonClick: (() -> Unit)?,
    onNoButtonClick: (() -> Unit)? = null,
    windowFeature: Int? = null
): AlertDialog {
    var dialog: AlertDialog? = null
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME)
            dialog?.show()
        else if (event == Lifecycle.Event.ON_PAUSE)
            dialog?.dismiss()
    }

    val alertDialog = MaterialAlertDialogBuilder(this).also { alert ->
        alert.setMessage(message)
        title?.let(alert::setTitle)

        onOkButtonClick?.let {
            alert.setPositiveButton(positiveButtonText) { _, _ ->
                dialog?.cancel()
                dialog = null
                removeObserver(observer)
                it.invoke()
            }
        }
        onNoButtonClick?.let {
            alert.setNegativeButton(negativeButtonText) { _, _ ->
                dialog?.cancel()
                dialog = null
                removeObserver(observer)
                it.invoke()
            }
        }
        alert.setCancelable(false)
    }.create()

    windowFeature?.let(alertDialog::requestWindowFeature)

    dialog = alertDialog
    addObserver(observer)
    alertDialog.show()
    return alertDialog
}