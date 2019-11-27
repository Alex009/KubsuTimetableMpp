package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.splash.Splash
import com.kubsu.timetable.presentation.splash.SplashEffectHandler
import com.kubsu.timetable.presentation.splash.splashUpdater
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val splashPresentationModule = Kodein.Module("splash_presentation") {
    bindGeneric<Feature<Splash.Action, Splash.SideEffect, Splash.State, Splash.Subscription>>() with factory { defaultState: Splash.State? ->
        TeaFeature(
            initialState = defaultState ?: Splash.State,
            updater = splashUpdater,
            effectHandler = SplashEffectHandler(instance()),
            onError = null
        )
    }
}