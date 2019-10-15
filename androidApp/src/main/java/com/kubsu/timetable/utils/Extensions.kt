package com.kubsu.timetable.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kubsu.timetable.R

inline fun <reified T> nameOf(): String =
    T::class.java.name

fun View.visibility(visibility: Visibility) = setVisibility(visibility.value)

enum class Visibility(val value: Int) {
    VISIBLE(View.VISIBLE),
    INVISIBLE(View.INVISIBLE),
    GONE(View.GONE)
}

fun TextView.showError(messageRes: Int) {
    error = null
    error = context.getString(messageRes)
    requestFocus()
}

fun Spinner.showError(messageRes: Int) {
    val view = selectedView as TextView
    view.error = null
    view.error = context.getString(messageRes)
    requestFocusFromTouch()
    performClick()
}

fun EditText.setOnEditorActionListenerToNext(next: EditText) =
    setOnEditorActionListener { _, _, _ ->
        next.requestFocus()
        next.setSelection(next.text.length)
        true
    }

/**
 * @return [NavController] if navigation allowed (view exists and state is not saved)
 */
fun Fragment.getNavControllerOrNull(): NavController? {
    if (isStateSaved)
        return null

    return view?.let(Navigation::findNavController)
}

fun Context.showMaterialAlert(
    markAsUsed: () -> Unit,
    message: String,
    title: String? = null,
    positiveButtonText: Int = android.R.string.ok,
    negativeButtonText: Int = android.R.string.cancel,
    onOkButtonClick: (() -> Unit)?,
    onNoButtonClick: (() -> Unit)? = null
) = MaterialAlertDialogBuilder(this, R.style.DayNight_Dialog_Alert).also { alert ->
    alert.setMessage(message)
    title?.let(alert::setTitle)

    onOkButtonClick?.let {
        alert.setPositiveButton(positiveButtonText) { _, _ ->
            markAsUsed()
            it.invoke()
        }
    }
    onNoButtonClick?.let {
        alert.setNegativeButton(negativeButtonText) { _, _ ->
            markAsUsed()
            it.invoke()
        }
    }

    alert.setCancelable(false)
}.show()