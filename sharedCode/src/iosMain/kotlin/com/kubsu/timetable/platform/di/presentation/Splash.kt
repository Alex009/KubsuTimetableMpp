package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.splash.Splash

fun splashFeature() = IosConnector(
    iosKodein.instanceDep<Splash.State?, Feature<Splash.Action, Splash.SideEffect, Splash.State, Splash.Subscription>>(
        null
    )
)