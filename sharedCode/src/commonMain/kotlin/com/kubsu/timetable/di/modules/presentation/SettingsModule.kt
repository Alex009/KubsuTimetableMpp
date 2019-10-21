package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.settings.*
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val settingsPresentationModule = Kodein.Module("settings_presentation") {
    bindGeneric<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature(
            initialState = State(),
            effectHandler = SettingsEffectHandler(instance(), instance()),
            update = settingsUpdater,
            onError = null
        )
    }
}