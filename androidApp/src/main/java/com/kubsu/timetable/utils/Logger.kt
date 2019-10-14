package com.kubsu.timetable.utils

import android.util.Log
import com.crashlytics.android.Crashlytics

interface Logger {
    val className: String
        get() = javaClass.simpleName

    fun info(message: String?, tag: String? = null) {
        val loggerTag = getTag(tag ?: className)
        Crashlytics.log(Log.INFO, loggerTag, message)
    }

    fun warn(message: String?, tag: String? = null) {
        val loggerTag = getTag(tag ?: className)
        Crashlytics.log(Log.WARN, loggerTag, message)
    }

    fun error(message: String?, exception: Exception?, tag: String? = null) {
        val loggerTag = getTag(tag ?: className)
        Crashlytics.log(Log.ERROR, loggerTag, message)
    }

    private fun getTag(className: String): String =
        if (className.length <= 23)
            className
        else
            className.substring(0, 23)
}