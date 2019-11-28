package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.registration.*
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val registrationPresentationModule = Kodein.Module("registration_presentation") {
    bindGeneric<Feature<RegistrationAction, RegistrationSideEffect, RegistrationState, RegistrationSubscription>>() with factory { defaultState: RegistrationState? ->
        TeaFeature(
            initialState = defaultState ?: RegistrationState(progress = false),
            effectHandler = RegistrationEffectHandler(instance()),
            updater = registrationUpdater,
            onError = null
        )
    }
}