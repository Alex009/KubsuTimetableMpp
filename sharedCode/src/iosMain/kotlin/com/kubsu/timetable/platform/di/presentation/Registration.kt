package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.registration.RegistrationAction
import com.kubsu.timetable.presentation.registration.RegistrationSideEffect
import com.kubsu.timetable.presentation.registration.RegistrationState
import com.kubsu.timetable.presentation.registration.RegistrationSubscription

fun registrationFeature() = IosConnector(
    iosKodein.instanceDep<RegistrationState?, Feature<RegistrationAction, RegistrationSideEffect, RegistrationState, RegistrationSubscription>>(
        null
    )
)