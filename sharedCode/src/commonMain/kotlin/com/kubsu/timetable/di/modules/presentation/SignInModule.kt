package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.signin.*
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val signInPresentationModule = Kodein.Module("sign_in_presentation") {
    bindGeneric<Feature<SignInAction, SignInSideEffect, SignInState, SignInSubscription>>() with factory { defaultState: SignInState? ->
        TeaFeature(
            initialState = defaultState ?: SignInState(progress = false),
            updater = signInUpdater,
            effectHandler = SignInEffectHandler(instance()),
            onError = null
        )
    }
}