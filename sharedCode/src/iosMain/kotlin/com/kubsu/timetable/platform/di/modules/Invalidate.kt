package com.kubsu.timetable.platform.di.modules

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.invalidate.Action
import com.kubsu.timetable.presentation.invalidate.SideEffect
import com.kubsu.timetable.presentation.invalidate.State
import com.kubsu.timetable.presentation.invalidate.Subscription

fun invalidateFeature() = IosConnector(
    iosKodein.instanceDep<State?, Feature<Action, SideEffect, State, Subscription>>(null)
)