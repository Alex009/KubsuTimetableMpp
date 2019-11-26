package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.presentation.settings.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val settingsPresentationModule = Kodein.Module("settings_presentation") {
    bindGeneric<Feature<Action, SideEffect, State, Subscription>>() with factory { defaultState: State? ->
        TeaFeature(
            initialState = defaultState ?: State(),
            effectHandler = SettingsEffectHandler(instance(), instance()),
            update = settingsUpdater,
            onError = null
        )
    }
}

fun KodeinAware.settingsFeature(defaultState: State? = null): Feature<Action, SideEffect, State, Subscription> =
    instanceDep(defaultState)