package com.kubsu.timetable.utils.observers

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class OnDestroyObserver(
    private val block: () -> Unit
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = block()
}