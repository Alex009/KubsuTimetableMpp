package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.Updater
import com.kubsu.timetable.presentation.timetable.*
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val timetablePresentationModule = Kodein.Module("timetable_presentation") {
    bind<EffectHandler<SideEffect, Action>>() with singleton { TimetableEffectHandler(instance()) }
    bind<Updater<State, Action, Subscription, SideEffect>>() with singleton { timetableUpdater }
    bind<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature<Action, SideEffect, State, Subscription>(
            initialState = State(
                progress = false,
                currentSubscription = null,
                universityInfoModel = null,
                numeratorTimetable = null,
                denominatorTimetable = null
            ),
            update = instance(),
            effectHandler = instance(),
            onError = null
        )
    }
}