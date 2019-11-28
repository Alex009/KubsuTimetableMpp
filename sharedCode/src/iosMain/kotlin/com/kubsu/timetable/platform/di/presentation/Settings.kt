package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.settings.SettingsAction
import com.kubsu.timetable.presentation.settings.SettingsSideEffect
import com.kubsu.timetable.presentation.settings.SettingsState
import com.kubsu.timetable.presentation.settings.SettingsSubscription

fun settingsFeature() = IosConnector(
    iosKodein.instanceDep<SettingsState?, Feature<SettingsAction, SettingsSideEffect, SettingsState, SettingsSubscription>>(
        null
    )
)