package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.invalidate.InvidateAction
import com.kubsu.timetable.presentation.invalidate.InvidateSideEffect
import com.kubsu.timetable.presentation.invalidate.InvidateState
import com.kubsu.timetable.presentation.invalidate.InvidateSubscription

fun invalidateFeature() = IosConnector(
    iosKodein.instanceDep<InvidateState?, Feature<InvidateAction, InvidateSideEffect, InvidateState, InvidateSubscription>>(
        null
    )
)