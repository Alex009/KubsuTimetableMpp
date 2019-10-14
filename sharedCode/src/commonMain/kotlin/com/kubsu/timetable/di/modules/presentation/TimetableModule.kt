package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.bindGeneric
import com.kubsu.timetable.presentation.timetable.*
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val timetablePresentationModule = Kodein.Module("timetable_presentation") {
    bindGeneric<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature<Action, SideEffect, State, Subscription>(
            initialState = State(
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