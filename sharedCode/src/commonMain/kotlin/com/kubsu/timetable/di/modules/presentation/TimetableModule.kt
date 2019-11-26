package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.timetable.*
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val timetablePresentationModule = Kodein.Module("timetable_presentation") {
    bindGeneric<Feature<Action, SideEffect, State, Subscription>>() with factory { defaultState: State? ->
        TeaFeature(
            initialState = defaultState ?: State(
                progress = false,
                currentSubscription = null,
                universityInfoModel = null,
                numeratorTimetable = null,
                denominatorTimetable = null
            ),
            update = timetableUpdater,
            effectHandler = TimetableEffectHandler(instance()),
            onError = null
        )
    }
}