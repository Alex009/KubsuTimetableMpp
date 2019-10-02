package com.kubsu.timetable

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

class App : Application(), KodeinAware {
    override val kodein: Kodein by lazy { appKodein(this) }

    override fun onCreate() {
        super.onCreate()
        val lifecycleCallbacks by kodein.instance<ActivityLifecycleCallbacks>()
        registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }
}