package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.signin.SignIn

fun signInFeature() = IosConnector(
    iosKodein.instanceDep<SignIn.State?, Feature<SignIn.Action, SignIn.SideEffect, SignIn.State, SignIn.Subscription>>(
        null
    )
)