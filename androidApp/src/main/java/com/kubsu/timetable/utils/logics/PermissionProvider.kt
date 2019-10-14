package com.kubsu.timetable.utils.logics

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionProvider {
    private const val requestCode = 93

    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.VIBRATE
    )

    fun hasAllPermissions(context: Context): Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    fun checkPermissions(activity: Activity) =
        ActivityCompat.requestPermissions(
            activity,
            permissions,
            requestCode
        )

    fun isAllPermissionsAllowed(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean =
        requestCode == PermissionProvider.requestCode
                && permissions.contentEquals(PermissionProvider.permissions)
                && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
}
