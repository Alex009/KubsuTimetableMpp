package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.fragments.registration.RegistrationFragment
import com.kubsu.timetable.instanceGeneric
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val registrationViewModule = Kodein.Module("registration_view_module") {
    bind() from provider {
        RegistrationFragment(teaFeature = instanceGeneric())
    }
}