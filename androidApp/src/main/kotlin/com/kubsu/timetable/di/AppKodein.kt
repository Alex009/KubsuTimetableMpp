package com.kubsu.timetable.di

import android.app.Application
import androidx.fragment.app.FragmentFactory
import com.kubsu.timetable.di.modules.view.*
import com.kubsu.timetable.di.platform.MyActivityLifecycleCallbacks
import com.kubsu.timetable.di.platform.MyFragmentFactory
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import platform.PlatformArgs
import platform.di.androidCommonKodein

fun appKodein(application: Application) = Kodein {
    extend(androidCommonKodein, copy = Copy.All)

    bind() from singleton { PlatformArgs(application.applicationContext) }

    importAll(
        splashViewModule,
        signInViewModule,
        bottomNavViewModule,
        settingsViewModule,
        registrationViewModule,
        createSubscriptionViewModule,
        subscriptionListViewModule,
        timetableViewModule
    )

    bind<Application.ActivityLifecycleCallbacks>() with singleton {
        MyActivityLifecycleCallbacks(instance(), instance())
    }

    bind<FragmentFactory>() with singleton {
        MyFragmentFactory(this)
    }
}