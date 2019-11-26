package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.presentation.signin.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val signInPresentationModule = Kodein.Module("sign_in_presentation") {
    bindGeneric<Feature<Action, SideEffect, State, Subscription>>() with factory { defaultState: State? ->
        TeaFeature(
            initialState = defaultState ?: State(progress = false),
            update = signInUpdater,
            effectHandler = SignInEffectHandler(instance()),
            onError = null
        )
    }
}

fun KodeinAware.signInFeature(defaultState: State? = null): Feature<Action, SideEffect, State, Subscription> =
    instanceDep(defaultState)