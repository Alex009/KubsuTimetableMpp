package com.kubsu.timetable.utils

import android.app.Activity
import android.content.Context
import android.view.MenuItem
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

inline fun <reified T> nameOf(): String =
    T::class.java.name

fun Context.getCompatColor(resId: Int) =
    ContextCompat.getColor(this, resId)

val BottomNavigationView.selectedItem: MenuItem
    get() = menu.findItem(selectedItemId)

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

fun Activity.closeApp(): Boolean {
    moveTaskToBack(true)
    finish()
    return true
}