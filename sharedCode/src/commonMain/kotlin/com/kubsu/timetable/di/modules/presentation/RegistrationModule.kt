package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.registration.Registration
import com.kubsu.timetable.presentation.registration.RegistrationEffectHandler
import com.kubsu.timetable.presentation.registration.registrationUpdater
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val registrationPresentationModule = Kodein.Module("registration_presentation") {
    bindGeneric<Feature<Registration.Action, Registration.SideEffect, Registration.State, Registration.Subscription>>() with factory { defaultState: Registration.State? ->
        TeaFeature(
            initialState = defaultState ?: Registration.State(progress = false),
            effectHandler = RegistrationEffectHandler(instance()),
            updater = registrationUpdater,
            onError = null
        )
    }
}