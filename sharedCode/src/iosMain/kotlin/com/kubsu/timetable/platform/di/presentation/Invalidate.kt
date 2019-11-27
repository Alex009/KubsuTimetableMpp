package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.invalidate.Invidate

fun invalidateFeature() = IosConnector(
    iosKodein.instanceDep<Invidate.State?, Feature<Invidate.Action, Invidate.SideEffect, Invidate.State, Invidate.Subscription>>(
        null
    )
)