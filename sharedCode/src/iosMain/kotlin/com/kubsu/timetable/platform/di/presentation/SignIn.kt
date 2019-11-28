package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.signin.SignInAction
import com.kubsu.timetable.presentation.signin.SignInSideEffect
import com.kubsu.timetable.presentation.signin.SignInState
import com.kubsu.timetable.presentation.signin.SignInSubscription

fun signInFeature() = IosConnector(
    iosKodein.instanceDep<SignInState?, Feature<SignInAction, SignInSideEffect, SignInState, SignInSubscription>>(
        null
    )
)