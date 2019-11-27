package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.timetable.Timetable

fun timetableFeature() = IosConnector(
    iosKodein.instanceDep<Timetable.State?, Feature<Timetable.Action, Timetable.SideEffect, Timetable.State, Timetable.Subscription>>(
        null
    )
)