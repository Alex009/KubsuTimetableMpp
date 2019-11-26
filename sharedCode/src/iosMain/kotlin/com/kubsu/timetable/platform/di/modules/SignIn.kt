package com.kubsu.timetable.platform.di.modules

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.signin.Action
import com.kubsu.timetable.presentation.signin.SideEffect
import com.kubsu.timetable.presentation.signin.State
import com.kubsu.timetable.presentation.signin.Subscription

fun signInFeature() = IosConnector(
    iosKodein.instanceDep<State?, Feature<Action, SideEffect, State, Subscription>>(null)
)