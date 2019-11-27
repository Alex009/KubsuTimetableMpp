package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.signin.SignIn
import com.kubsu.timetable.presentation.signin.SignInEffectHandler
import com.kubsu.timetable.presentation.signin.signInUpdater
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val signInPresentationModule = Kodein.Module("sign_in_presentation") {
    bindGeneric<Feature<SignIn.Action, SignIn.SideEffect, SignIn.State, SignIn.Subscription>>() with factory { defaultState: SignIn.State? ->
        TeaFeature(
            initialState = defaultState ?: SignIn.State(progress = false),
            updater = signInUpdater,
            effectHandler = SignInEffectHandler(instance()),
            onError = null
        )
    }
}