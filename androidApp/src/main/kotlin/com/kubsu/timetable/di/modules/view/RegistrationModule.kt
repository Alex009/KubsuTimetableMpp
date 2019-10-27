package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.extensions.instanceGeneric
import com.kubsu.timetable.fragments.registration.RegistrationFragment
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val registrationViewModule = Kodein.Module("registration_view_module") {
    bind() from provider {
        RegistrationFragment(featureFactory = ::instanceGeneric)
    }
}