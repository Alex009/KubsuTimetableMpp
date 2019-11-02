package com.kubsu.timetable.di.platform

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import com.kubsu.timetable.InformationSynchronizer
import com.kubsu.timetable.base.AppActivity

class MyActivityLifecycleCallbacks(
    private val fragmentFactory: FragmentFactory,
    private val informationSynchronizer: InformationSynchronizer
) : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        (activity as? AppActivity)?.let {
            it.supportFragmentManager.fragmentFactory = fragmentFactory
            it.informationSynchronizer = informationSynchronizer
        }
    }

    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
}