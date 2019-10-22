package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.extensions.instanceGeneric
import com.kubsu.timetable.fragments.bottomnav.timetable.TimetableFragment
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider

val timetableViewModule = Kodein.Module("timetable_view_module") {
    bind() from provider {
        TimetableFragment(
            teaFeature = instanceGeneric(),
            displayedSubscriptionStorage = instance()
        )
    }
}