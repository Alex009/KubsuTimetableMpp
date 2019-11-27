package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.registration.Registration

fun registrationFeature() = IosConnector(
    iosKodein.instanceDep<Registration.State?, Feature<Registration.Action, Registration.SideEffect, Registration.State, Registration.Subscription>>(
        null
    )
)