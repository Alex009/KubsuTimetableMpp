package com.kubsu.timetable.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.kubsu.timetable.BaseNavGraphDirections
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.R
import com.kubsu.timetable.firebase.NotAuthenticatedException
import com.kubsu.timetable.firebase.ParsingException
import com.kubsu.timetable.firebase.UnknownResponseException
import com.kubsu.timetable.utils.Logger
import com.kubsu.timetable.utils.safeNavigate
import com.kubsu.timetable.utils.safePopBackStack
import com.kubsu.timetable.utils.ui.materialAlert

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId), Logger {
    open fun popBackStack(): Boolean = safePopBackStack()

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
                        popBackStack()
                    }
                }
            )
    }

    protected fun notifyUserOfFailure(failure: DataFailure) =
        when (failure) {
            is DataFailure.ConnectionToRepository -> {
                requireActivity().materialAlert(
                    message = getString(R.string.error_connecting),
                    onOkButtonClick = {}
                )
                Unit
            }

            is DataFailure.NotAuthenticated -> {
                error(
                    message = failure.debugMessage,
                    exception = NotAuthenticatedException(failure.debugMessage)
                )
                requireActivity().materialAlert(
                    message = getString(R.string.not_authenticated),
                    onOkButtonClick = {
                        safeNavigate(BaseNavGraphDirections.actionGlobalSignInFragment())
                    }
                )
                Unit
            }

            is DataFailure.ParsingError -> {
                error(
                    message = failure.debugMessage,
                    exception = ParsingException(failure.debugMessage)
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
}