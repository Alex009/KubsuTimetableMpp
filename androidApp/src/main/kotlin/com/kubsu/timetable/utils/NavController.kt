package com.kubsu.timetable.utils

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.egroden.teaco.identity
import com.kubsu.timetable.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

fun Fragment.safePopBackStack(): Boolean {
    if (isStateSaved) return false
    return activity?.popBackStackOrClose() ?: false
}

fun ComponentActivity.popBackStackOrClose(): Boolean =
    try {
        popBackStackOrClose(R.id.bottom_nav_host_fragment)
    } catch (e: Exception) {
        popBackStackOrClose(R.id.nav_host_fragment)
    }

private val clickCount = AtomicInteger(0)
private val job = AtomicReference<Job?>(null)

private fun ComponentActivity.popBackStackOrClose(viewId: Int): Boolean =
    findNavController(viewId)
        .popBackStack()
        .takeIf(::identity)
        ?: run {
            val old = job.getAndSet(
                GlobalScope.launch {
                    delay(1000)
                    clickCount.set(0)
                }
            )
            old?.cancel()
            val count = clickCount.incrementAndGet()

            if (count == 2) {
                closeApp()
            } else {
                toast(R.string.сlick_again_to_close)
                true
            }
        }

fun Fragment.safeNavigate(navDirections: NavDirections) {
    val activity = activity ?: return
    return try {
        findNavController(activity, R.id.bottom_nav_host_fragment)
            .navigate(navDirections)
    } catch (e: Exception) {
        findNavController(activity, R.id.nav_host_fragment)
            .navigate(navDirections)
    }
}