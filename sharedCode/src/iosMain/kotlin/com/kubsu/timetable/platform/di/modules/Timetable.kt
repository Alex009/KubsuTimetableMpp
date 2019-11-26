package com.kubsu.timetable.platform.di.modules

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.timetable.Action
import com.kubsu.timetable.presentation.timetable.SideEffect
import com.kubsu.timetable.presentation.timetable.State
import com.kubsu.timetable.presentation.timetable.Subscription

fun timetableFeature() = IosConnector(
    iosKodein.instanceDep<State?, Feature<Action, SideEffect, State, Subscription>>(null)
)