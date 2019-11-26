package com.kubsu.timetable.platform.di.modules

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.splash.Action
import com.kubsu.timetable.presentation.splash.SideEffect
import com.kubsu.timetable.presentation.splash.State
import com.kubsu.timetable.presentation.splash.Subscription

fun splashFeature() = IosConnector(
    iosKodein.instanceDep<State?, Feature<Action, SideEffect, State, Subscription>>(null)
)