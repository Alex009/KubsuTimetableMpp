package com.kubsu.timetable.base

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.kubsu.timetable.di.appKodein
import com.kubsu.timetable.utils.logics.activityLifecycleInjector
import io.fabric.sdk.android.Fabric
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

class App : Application(), KodeinAware {
    override val kodein: Kodein by lazy { appKodein(this) }

    override fun onCreate() {
        super.onCreate()
        val lifecycleCallbacks by kodein.instance<ActivityLifecycleCallbacks>()
        registerActivityLifecycleCallbacks(lifecycleCallbacks)
        registerActivityLifecycleCallbacks(activityLifecycleInjector)

        Fabric.with(this, Crashlytics())
    }
}