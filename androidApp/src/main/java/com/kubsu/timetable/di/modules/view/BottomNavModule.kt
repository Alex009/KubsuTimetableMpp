package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.fragments.bottomnav.BottomNavFragment
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider

val bottomNavViewModule = Kodein.Module("bottom_nav_view_module") {
    bind() from provider {
        BottomNavFragment(displayedSubscriptionStorage = instance())
    }
}