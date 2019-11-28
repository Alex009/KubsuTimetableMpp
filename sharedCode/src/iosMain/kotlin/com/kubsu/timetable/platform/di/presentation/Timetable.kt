package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.timetable.TimetableAction
import com.kubsu.timetable.presentation.timetable.TimetableSideEffect
import com.kubsu.timetable.presentation.timetable.TimetableState
import com.kubsu.timetable.presentation.timetable.TimetableSubscription

fun timetableFeature() = IosConnector(
    iosKodein.instanceDep<TimetableState?, Feature<TimetableAction, TimetableSideEffect, TimetableState, TimetableSubscription>>(
        null
    )
)