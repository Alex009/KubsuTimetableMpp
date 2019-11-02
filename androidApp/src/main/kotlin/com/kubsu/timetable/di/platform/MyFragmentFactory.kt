package com.kubsu.timetable.di.platform

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.kubsu.timetable.fragments.bottomnav.BottomNavFragment
import com.kubsu.timetable.fragments.bottomnav.settings.SettingsFragment
import com.kubsu.timetable.fragments.bottomnav.subscription.list.SubscriptionListFragment
import com.kubsu.timetable.fragments.bottomnav.timetable.TimetableFragment
import com.kubsu.timetable.fragments.registration.RegistrationFragment
import com.kubsu.timetable.fragments.signin.SignInFragment
import com.kubsu.timetable.fragments.splash.SplashFragment
import com.kubsu.timetable.fragments.subscription.create.CreateSubscriptionFragment
import com.kubsu.timetable.utils.nameOf
import org.kodein.di.DKodeinAware
import org.kodein.di.erased.instance

class MyFragmentFactory(private val kodein: DKodeinAware) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment = with(kodein) {
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