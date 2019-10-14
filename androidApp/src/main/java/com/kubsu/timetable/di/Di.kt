package com.kubsu.timetable.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kubsu.timetable.base.App
import di.androidModule
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun appKodein(app: App) = Kodein {
    import(androidModule(app))

    bind<Application.ActivityLifecycleCallbacks>() with singleton {
        object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }
            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
        }
    }
}