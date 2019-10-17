package com.kubsu.timetable.utils

import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment

fun BaseFragment.safeNavigateUp(): Boolean {
    if (isStateSaved) return false
    val activity = appActivity ?: return false
    return try {
        Navigation
            .findNavController(activity, R.id.nav_host_fragment)
            .navigateUp()
    } catch (e: Exception) {
        Navigation
            .findNavController(activity, R.id.bottom_nav_host_fragment)
            .navigateUp()
    }
}

fun BaseFragment.safeNavigate(navDirections: NavDirections): Unit {
    val activity = appActivity ?: return
    return try {
        Navigation
            .findNavController(activity, R.id.nav_host_fragment)
            .navigate(navDirections)
    } catch (e: Exception) {
        Navigation
            .findNavController(activity, R.id.bottom_nav_host_fragment)
            .navigate(navDirections)
    }
}