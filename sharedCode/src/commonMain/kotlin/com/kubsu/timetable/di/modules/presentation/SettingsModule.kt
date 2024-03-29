package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.settings.*
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val settingsPresentationModule = Kodein.Module("settings_presentation") {
    bindGeneric<Feature<SettingsAction, SettingsSideEffect, SettingsState, SettingsSubscription>>() with factory { defaultState: SettingsState? ->
        TeaFeature(
            initialState = defaultState ?: SettingsState,
            effectHandler = SettingsEffectHandler(instance(), instance()),
            updater = settingsUpdater,
            onError = null
        )
    }
}