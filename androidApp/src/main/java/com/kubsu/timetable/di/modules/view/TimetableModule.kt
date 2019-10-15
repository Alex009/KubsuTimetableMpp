package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.fragments.bottomnav.timetable.TimetableFragment
import com.kubsu.timetable.instanceGeneric
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val timetableViewModule = Kodein.Module("timetable_view_module") {
    bind() from provider {
        TimetableFragment(teaFeature = instanceGeneric())
    }
}