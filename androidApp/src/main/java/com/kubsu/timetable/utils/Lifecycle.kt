package com.kubsu.timetable.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleObserver

object LifecycleObserversStorage {
    val observers = ArrayList<LifecycleObserver>()
}

val activityLifecycleInjector = object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        with(activity as ComponentActivity) {
            LifecycleObserversStorage.observers.forEach(lifecycle::addObserver)
        }
    }

    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}

fun ComponentActivity.addObserver(observer: LifecycleObserver) {
    lifecycle.addObserver(observer)
    LifecycleObserversStorage.observers.add(observer)
}

fun ComponentActivity.removeObserver(observer: LifecycleObserver) {
    lifecycle.removeObserver(observer)
    LifecycleObserversStorage.observers.remove(observer)
}