package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.settings.Settings

fun settingsFeature() = IosConnector(
    iosKodein.instanceDep<Settings.State?, Feature<Settings.Action, Settings.SideEffect, Settings.State, Settings.Subscription>>(
        null
    )
)