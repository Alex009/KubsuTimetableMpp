package com.kubsu.timetable.base

import android.app.Application
import androidx.fragment.app.FragmentFactory
import com.crashlytics.android.Crashlytics
import com.kubsu.timetable.di.modules.view.*
import com.kubsu.timetable.di.platform.MyActivityLifecycleCallbacks
import com.kubsu.timetable.di.platform.MyFragmentFactory
import com.kubsu.timetable.platform.di.androidCommonKodein
import com.kubsu.timetable.utils.logics.activityLifecycleInjector
import io.fabric.sdk.android.Fabric
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

class App : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        extend(androidCommonKodein(this@App), copy = Copy.All)

        importAll(
            splashViewModule,
            signInViewModule,
            bottomNavViewModule,
            settingsViewModule,
            registrationViewModule,
            invalidateViewModule,
            createSubscriptionViewModule,
            subscriptionListViewModule,
            timetableViewModule
        )

        bind<ActivityLifecycleCallbacks>() with singleton {
            MyActivityLifecycleCallbacks(instance(), instance())
        }

        bind<FragmentFactory>() with singleton {
            MyFragmentFactory(this)
        }
    }

    override fun onCreate() {
        super.onCreate()

        val lifecycleCallbacks by kodein.instance<ActivityLifecycleCallbacks>()
        registerActivityLifecycleCallbacks(lifecycleCallbacks)
        registerActivityLifecycleCallbacks(activityLifecycleInjector)

        Fabric.with(this, Crashlytics())
    }
}