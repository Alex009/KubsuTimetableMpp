package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.timetable.Timetable
import com.kubsu.timetable.presentation.timetable.TimetableEffectHandler
import com.kubsu.timetable.presentation.timetable.timetableUpdater
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val timetablePresentationModule = Kodein.Module("timetable_presentation") {
    bindGeneric<Feature<Timetable.Action, Timetable.SideEffect, Timetable.State, Timetable.Subscription>>() with factory { defaultState: Timetable.State? ->
        TeaFeature(
            initialState = defaultState ?: Timetable.State(
                progress = false,
                currentSubscription = null,
                universityInfoModel = null,
                numeratorTimetable = null,
                denominatorTimetable = null
            ),
            updater = timetableUpdater,
            effectHandler = TimetableEffectHandler(instance()),
            onError = null
        )
    }
}