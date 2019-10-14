package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.Updater
import com.kubsu.timetable.presentation.registration.*
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val registrationPresentationModule = Kodein.Module("registration_presentation") {
    bind<EffectHandler<SideEffect, Action>>() with singleton { RegistrationEffectHandler(instance()) }
    bind<Updater<State, Action, Subscription, SideEffect>>() with singleton { registrationUpdater }
    bind<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature<Action, SideEffect, State, Subscription>(
            initialState = State(progress = false),
            effectHandler = instance(),
            update = instance(),
            onError = null
        )
    }
}