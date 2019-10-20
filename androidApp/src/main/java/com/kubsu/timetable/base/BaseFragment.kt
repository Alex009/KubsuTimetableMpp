package com.kubsu.timetable.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.R
import com.kubsu.timetable.firebase.NotAuthenticatedException
import com.kubsu.timetable.firebase.UnknownResponseException
import com.kubsu.timetable.utils.Logger
import com.kubsu.timetable.utils.observers.OnDestroyObserver
import com.kubsu.timetable.utils.safeNavigateUp
import ru.whalemare.sheetmenu.ActionItem
import ru.whalemare.sheetmenu.SheetMenu

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId), Logger {
    open fun navigateUp(): Boolean = safeNavigateUp()

    val appActivity: AppActivity?
        get() = activity as? AppActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navigateUp()
                    }
                }
            )
    }

    protected fun notifyUserOfFailure(failure: DataFailure) =
        when (failure) {
            is DataFailure.ConnectionToRepository -> {
                materialAlert(
                    message = R.string.error_connecting,
                    onOkButtonClick = {}
                )
            }

            is DataFailure.NotAuthenticated -> {
                error(
                    message = failure.debugMessage,
                    exception = NotAuthenticatedException(failure.debugMessage)
                )
                materialAlert(
                    message = R.string.not_authenticated,
                    onOkButtonClick = {}
                )
            }

            is DataFailure.UnknownResponse -> {
                error(
                    message = failure.debugMessage,
                    exception = UnknownResponseException(
                        code = failure.code,
                        body = failure.body,
                        message = failure.debugMessage
                    )
                )
            }
        }

    /**
     * Скрыть отображаемое сообщение, какое бы оно ни было: snack bar, sheet menu, alert, toast.
     */
    protected fun hideDisplayedMessage() {
        dismissSnackBar()
        dismissSheetMenu()
        dismissAlert()
        cancelToast()
    }

    /*
     *************** Snackbar **************
     */

    private var snackbar: Snackbar? = null

    protected fun snackBar(
        view: View,
        textRes: Int,
        actionTextRes: Int,
        action: () -> Unit
    ) {
        hideDisplayedMessage()
        snackbar = Snackbar
            .make(view, textRes, Snackbar.LENGTH_INDEFINITE)
            .also {
                it.setAction(actionTextRes) { action() }
                it.show()
            }
    }

    private fun dismissSnackBar() {
        snackbar?.dismiss()
        snackbar = null
    }

    /*
     *************** Sheet menu **************
     */

    private var sheetMenu: SheetMenu? = null

    protected fun sheetMenu(
        context: Context,
        titleId: Int = 0,
        title: String? = "",
        menu: Int = 0,
        onClick: (ActionItem) -> Unit,
        showIcons: Boolean = false
    ) {
        hideDisplayedMessage()
        SheetMenu(
            context = context,
            menu = menu,
            title = title ?: getString(titleId),
            onClick = onClick,
            showIcons = showIcons
        ).also {
            sheetMenu = it
            it.show(context, lifecycle)
        }
    }

    private fun dismissSheetMenu() {
        sheetMenu?.dialog?.dismiss()
        sheetMenu = null
    }

    /*
     *************** Alert-ы **************
     */

    private var alertDialog: AlertDialog? = null

    protected fun materialAlert(
        message: Int,
        title: Int? = null,
        positiveButtonText: Int = android.R.string.ok,
        negativeButtonText: Int = android.R.string.cancel,
        onOkButtonClick: (() -> Unit)?,
        onNoButtonClick: (() -> Unit)? = null
    ) =
        materialAlert(
            message = getString(message),
            title = title?.let { getString(it) },
            positiveButtonText = positiveButtonText,
            negativeButtonText = negativeButtonText,
            onOkButtonClick = onOkButtonClick,
            onNoButtonClick = onNoButtonClick
        )

    protected fun materialAlert(
        message: String,
        title: String? = null,
        positiveButtonText: Int = android.R.string.ok,
        negativeButtonText: Int = android.R.string.cancel,
        onOkButtonClick: (() -> Unit)?,
        onNoButtonClick: (() -> Unit)? = null
    ) {
        hideDisplayedMessage()
        val alertDialog =
            MaterialAlertDialogBuilder(context).also { alert ->
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

                alert.setCancelable(false)
            }.show()

        this.alertDialog = alertDialog

        lifecycle.addObserver(OnDestroyObserver {
            alertDialog.dismiss()
        })
    }

    private fun dismissAlert() {
        alertDialog?.dismiss()
        alertDialog = null
    }

    /*
     *************** Toast-ы **************
     */

    private var toast: Toast? = null

    protected fun toast(
        message: Int,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        hideDisplayedMessage()
        Toast.makeText(context, message, duration).let {
            toast = it
            it.show()
        }
    }

    private fun cancelToast() {
        toast?.cancel()
        toast = null
    }
}