package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.extensions.instanceGeneric
import com.kubsu.timetable.fragments.invalidate.InvalidateFragment
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val invalidateViewModule = Kodein.Module("invalidate_view_module") {
    bind() from provider {
        InvalidateFragment(featureFactory = ::instanceGeneric)
    }
}