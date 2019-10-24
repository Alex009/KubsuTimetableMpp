package com.kubsu.timetable.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.kubsu.timetable.base.AppActivity
import com.kubsu.timetable.di.modules.view.*
import com.kubsu.timetable.extensions.instanceGeneric
import com.kubsu.timetable.fragments.bottomnav.BottomNavFragment
import com.kubsu.timetable.fragments.bottomnav.settings.SettingsFragment
import com.kubsu.timetable.fragments.bottomnav.subscription.list.SubscriptionListFragment
import com.kubsu.timetable.fragments.bottomnav.timetable.TimetableFragment
import com.kubsu.timetable.fragments.registration.RegistrationFragment
import com.kubsu.timetable.fragments.signin.SignInFragment
import com.kubsu.timetable.fragments.splash.SplashFragment
import com.kubsu.timetable.fragments.subscription.create.CreateSubscriptionFragment
import com.kubsu.timetable.utils.nameOf
import di.androidCommonKodein
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import platform.PlatformArgs

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
        object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                (activity as? AppActivity)?.let {
                    it.supportFragmentManager.fragmentFactory = instance()
                    it.teaFeature = instanceGeneric()
                }
            }

            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
        }
    }

    bind() from singleton {
        object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
                when (className) {
                    nameOf<SplashFragment>() ->
                        instance<SplashFragment>()
                    nameOf<SignInFragment>() ->
                        instance<SignInFragment>()
                    nameOf<RegistrationFragment>() ->
                        instance<RegistrationFragment>()
                    nameOf<BottomNavFragment>() ->
                        instance<BottomNavFragment>()
                    nameOf<SettingsFragment>() ->
                        instance<SettingsFragment>()
                    nameOf<CreateSubscriptionFragment>() ->
                        instance<CreateSubscriptionFragment>()
                    nameOf<SubscriptionListFragment>() ->
                        instance<SubscriptionListFragment>()
                    nameOf<TimetableFragment>() ->
                        instance<TimetableFragment>()
                    else ->
                        super.instantiate(classLoader, className)
                }
        }
    }
}