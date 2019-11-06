package com.kubsu.timetable.utils

import android.content.Context
import android.view.MenuItem
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout

inline fun <reified T> nameOf(): String =
    T::class.java.name

fun ComponentActivity.toast(resId: Int) =
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.getCompatColor(resId: Int) =
    ContextCompat.getColor(this, resId)

fun Context.convertDpToPx(dp: Int): Int =
    (dp * resources.displayMetrics.density + 0.5f).toInt()

fun ContentLoadingProgressBar.setVisibleStatus(value: Boolean) =
    if (value) show() else hide()

val BottomNavigationView.selectedItem: MenuItem
    get() = menu.findItem(selectedItemId)

fun TextInputLayout.removeErrorAfterNewText() {
    editText?.doOnTextChanged { text, _, _, _ ->
        if ((text ?: "").isNotEmpty())
            error = null
    }
}

fun TextInputLayout.removeFocusAfterEmptyText() {
    editText?.doOnTextChanged { text, _, _, _ ->
        if ((text ?: "").isEmpty())
            clearFocus()
    }
}

fun TextInputLayout.showErrorMessage(messageRes: Int) {
    error = null
    error = context.getString(messageRes)
    requestFocusFromTouch()
}

val TextInputLayout.text: String
    inline get() = editText?.text.toString()

fun Spinner.showErrorMessage(messageRes: Int) {
    val view = selectedView as TextView
    view.error = null
    view.error = context.getString(messageRes)
    requestFocusFromTouch()
    performClick()
}

fun ComponentActivity.closeApp(): Boolean {
    moveTaskToBack(true)
    finish()
    return true
}