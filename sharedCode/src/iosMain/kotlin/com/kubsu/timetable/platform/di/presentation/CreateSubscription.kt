package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.subscription.create.CreateSub

fun createSubscriptionFeature() = IosConnector(
    iosKodein.instanceDep<CreateSub.State?, Feature<CreateSub.Action, CreateSub.SideEffect, CreateSub.State, CreateSub.Subscription>>(
        null
    )
)