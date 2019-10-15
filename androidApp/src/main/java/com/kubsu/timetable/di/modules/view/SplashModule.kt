package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.fragments.splash.SplashFragment
import com.kubsu.timetable.instanceGeneric
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val splashViewModule = Kodein.Module("splash_view_module") {
    bind() from provider {
        SplashFragment(teaFeature = instanceGeneric())
    }
}