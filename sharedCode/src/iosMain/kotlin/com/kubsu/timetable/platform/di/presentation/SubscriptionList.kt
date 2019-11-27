package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.subscription.list.SubList

fun subscriptionListFeature() = IosConnector(
    iosKodein.instanceDep<SubList.State?, Feature<SubList.Action, SubList.SideEffect, SubList.State, SubList.Subscription>>(
        null
    )
)