package com.kubsu.timetable.platform.di.modules

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.registration.Action
import com.kubsu.timetable.presentation.registration.SideEffect
import com.kubsu.timetable.presentation.registration.State
import com.kubsu.timetable.presentation.registration.Subscription

fun registrationFeature() = IosConnector(
    iosKodein.instanceDep<State?, Feature<Action, SideEffect, State, Subscription>>(null)
)