package com.kubsu.timetable.utils.logics

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object DarkThemeStatus {
    fun applyPreviousTheme(context: Context) =
        applyTheme(
            enabled = getCacheStatus(
                context
            )
        )

    fun applyNewTheme(context: Context, enabled: Boolean) {
        if (enabled != getCacheStatus(
                context
            )
        ) // Если в префах лежит другое значение
            setCacheStatus(
                context,
                enabled
            )  // обновляем значение

        applyTheme(enabled)
    }

    private fun applyTheme(enabled: Boolean) =
        AppCompatDelegate.setDefaultNightMode(
            if (enabled)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

    private const val label = "dark_theme_status"

    fun getCacheStatus(context: Context): Boolean =
        context
            .getPrivatePreferences()
            .getBoolean(label, false)

    private fun setCacheStatus(context: Context, enabled: Boolean) =
        context
            .getPrivatePreferences()
            .edit { putBoolean(label, enabled) }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Context.getPrivatePreferences(): SharedPreferences =
        getSharedPreferences("DarkTheme", Context.MODE_PRIVATE)
}
