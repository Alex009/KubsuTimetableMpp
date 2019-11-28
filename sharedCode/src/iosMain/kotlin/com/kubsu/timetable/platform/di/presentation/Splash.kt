package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.splash.SplashAction
import com.kubsu.timetable.presentation.splash.SplashSideEffect
import com.kubsu.timetable.presentation.splash.SplashState
import com.kubsu.timetable.presentation.splash.SplashSubscription

fun splashFeature() = IosConnector(
    iosKodein.instanceDep<SplashState?, Feature<SplashAction, SplashSideEffect, SplashState, SplashSubscription>>(
        null
    )
)