package com.kubsu.timetable.platform.di.modules

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.subscription.create.Action
import com.kubsu.timetable.presentation.subscription.create.SideEffect
import com.kubsu.timetable.presentation.subscription.create.State
import com.kubsu.timetable.presentation.subscription.create.Subscription

fun createSubscriptionFeature() = IosConnector(
    iosKodein.instanceDep<State?, Feature<Action, SideEffect, State, Subscription>>(null)
)