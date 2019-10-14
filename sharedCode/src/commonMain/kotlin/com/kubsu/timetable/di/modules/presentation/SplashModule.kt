package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.bindGeneric
import com.kubsu.timetable.presentation.splash.*
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val splashPresentationModule = Kodein.Module("splash_presentation") {
    bindGeneric<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature(
            initialState = State(),
            update = splashUpdater,
            effectHandler = SplashEffectHandler(instance()),
            onError = null
        )
    }
}