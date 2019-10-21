package com.kubsu.timetable.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.egroden.teaco.identity
import com.kubsu.timetable.R
import com.kubsu.timetable.base.AppActivity

fun Fragment.safePopBackStack(): Boolean {
    if (isStateSaved) return false
    return (activity as? AppActivity)?.run {
        try {
            popBackStackOrClose(R.id.nav_host_fragment)
        } catch (e: Exception) {
            popBackStackOrClose(R.id.bottom_nav_host_fragment)
        }
    } ?: false
}

private fun AppActivity.popBackStackOrClose(viewId: Int): Boolean =
    findNavController(viewId)
        .popBackStack()
        .takeIf(::identity)
        ?: closeApp()

fun Fragment.safeNavigate(navDirections: NavDirections) {
    val activity = activity ?: return
    return try {
        findNavController(activity, R.id.nav_host_fragment)
            .navigate(navDirections)
    } catch (e: Exception) {
        findNavController(activity, R.id.bottom_nav_host_fragment)
            .navigate(navDirections)
    }
}