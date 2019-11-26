package com.kubsu.timetable.platform.di.modules

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.subscription.list.Action
import com.kubsu.timetable.presentation.subscription.list.SideEffect
import com.kubsu.timetable.presentation.subscription.list.State
import com.kubsu.timetable.presentation.subscription.list.Subscription

fun subscriptionFeature() = IosConnector(
    iosKodein.instanceDep<State?, Feature<Action, SideEffect, State, Subscription>>(null)
)