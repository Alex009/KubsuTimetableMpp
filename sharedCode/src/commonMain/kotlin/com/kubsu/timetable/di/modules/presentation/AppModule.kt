package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.app.*
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val appPresentationModule = Kodein.Module("app_presentation") {
    bindGeneric<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature(
            initialState = State(),
            update = appUpdater,
            effectHandler = AppEffectHandler(instance(), instance()),
            onError = null
        )
    }
}