package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.invalidate.*
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val invalidatePresentationModule = Kodein.Module("invalidate_presentation") {
    bindGeneric<Feature<InvidateAction, InvidateSideEffect, InvidateState, InvidateSubscription>>() with factory { defaultState: InvidateState? ->
        TeaFeature(
            initialState = defaultState ?: InvidateState(progress = false),
            effectHandler = InvalidateEffectHandler(instance()),
            updater = invalidateUpdater,
            onError = null
        )
    }
}