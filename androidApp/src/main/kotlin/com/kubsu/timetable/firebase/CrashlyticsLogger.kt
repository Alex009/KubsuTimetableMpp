package com.kubsu.timetable.firebase

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.kubsu.timetable.DataFailure

object CrashlyticsLogger {
    fun logFailureToCrashlytics(failure: DataFailure, tag: String) =
        when (failure) {
            is DataFailure.ConnectionToRepository -> Unit
            is DataFailure.NotAuthenticated ->
                error(
                    tag = tag,
                    message = failure.debugMessage,
                    exception = NotAuthenticatedException(failure.debugMessage)
                )
            is DataFailure.ParsingError ->
                error(
                    tag = tag,
                    message = failure.debugMessage,
                    exception = ParsingException(failure.debugMessage)
                )
            is DataFailure.UnknownResponse ->
                error(
                    tag = tag,
                    message = failure.debugMessage,
                    exception = UnknownResponseException(
                        code = failure.code,
                        body = failure.body,
                        message = failure.debugMessage
                    )
                )
        }

    fun error(message: String?, exception: Exception?, tag: String) {
        val loggerTag = getTag(tag)
        Crashlytics.log(Log.ERROR, loggerTag, message)
        Crashlytics.logException(exception)
    }

    private fun getTag(className: String): String =
        if (className.length <= 23)
            className
        else
            className.substring(0, 23)
}