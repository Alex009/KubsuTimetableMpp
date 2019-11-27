package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.invalidate.InvalidateEffectHandler
import com.kubsu.timetable.presentation.invalidate.Invidate
import com.kubsu.timetable.presentation.invalidate.invalidateUpdater
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val invalidatePresentationModule = Kodein.Module("invalidate_presentation") {
    bindGeneric<Feature<Invidate.Action, Invidate.SideEffect, Invidate.State, Invidate.Subscription>>() with factory { defaultState: Invidate.State? ->
        TeaFeature(
            initialState = defaultState ?: Invidate.State(progress = false),
            effectHandler = InvalidateEffectHandler(instance()),
            updater = invalidateUpdater,
            onError = null
        )
    }
}