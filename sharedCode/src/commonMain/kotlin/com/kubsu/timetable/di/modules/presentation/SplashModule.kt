package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.splash.*
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val splashPresentationModule = Kodein.Module("splash_presentation") {
    bindGeneric<Feature<SplashAction, SplashSideEffect, SplashState, SplashSubscription>>() with factory { defaultState: SplashState? ->
        TeaFeature(
            initialState = defaultState ?: SplashState,
            updater = splashUpdater,
            effectHandler = SplashEffectHandler(instance()),
            onError = null
        )
    }
}