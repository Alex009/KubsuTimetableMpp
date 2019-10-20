package com.kubsu.timetable.utils

import android.content.Context
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat

inline fun <reified T> nameOf(): String =
    T::class.java.name

fun Context.getCompatColor(resId: Int) =
    ContextCompat.getColor(this, resId)

fun View.visibility(visibility: Visibility) = setVisibility(visibility.value)

enum class Visibility(val value: Int) {
    VISIBLE(View.VISIBLE),
    INVISIBLE(View.INVISIBLE),
    GONE(View.GONE)
}

fun TextView.showErrorMessage(messageRes: Int) {
    error = null
    error = context.getString(messageRes)
    requestFocusFromTouch()
}

fun Spinner.showErrorMessage(messageRes: Int) {
    val view = selectedView as TextView
    view.error = null
    view.error = context.getString(messageRes)
    requestFocusFromTouch()
    performClick()
}