package com.kubsu.timetable.utils.logics

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

object Keyboard {
    private var isKeyboardShowing = false

    fun observeKeyboardVisibleStatus(parentView: View): Unit =
        parentView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            parentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = parentView.rootView.height

            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing)
                    isKeyboardShowing = true
            } else {
                // keyboard is closed
                if (isKeyboardShowing)
                    isKeyboardShowing = false
            }
        }

    fun show(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!isKeyboardShowing)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hide(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isKeyboardShowing)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
