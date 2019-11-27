package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.settings.Settings
import com.kubsu.timetable.presentation.settings.SettingsEffectHandler
import com.kubsu.timetable.presentation.settings.settingsUpdater
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val settingsPresentationModule = Kodein.Module("settings_presentation") {
    bindGeneric<Feature<Settings.Action, Settings.SideEffect, Settings.State, Settings.Subscription>>() with factory { defaultState: Settings.State? ->
        TeaFeature(
            initialState = defaultState ?: Settings.State,
            effectHandler = SettingsEffectHandler(instance(), instance()),
            updater = settingsUpdater,
            onError = null
        )
    }
}