package com.kubsu.timetable.platform.di.modules

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.settings.Action
import com.kubsu.timetable.presentation.settings.SideEffect
import com.kubsu.timetable.presentation.settings.State
import com.kubsu.timetable.presentation.settings.Subscription

fun settingsFeature() = IosConnector(
    iosKodein.instanceDep<State?, Feature<Action, SideEffect, State, Subscription>>(null)
)