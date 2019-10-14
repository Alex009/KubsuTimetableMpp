package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.bindGeneric
import com.kubsu.timetable.presentation.signin.*
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val signInPresentationModule = Kodein.Module("sign_in_presentation") {
    bindGeneric<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature(
            initialState = State(progress = false),
            update = signInUpdater,
            effectHandler = SignInEffectHandler(instance()),
            onError = null
        )
    }
}