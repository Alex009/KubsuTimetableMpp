package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.subscription.list.SubListAction
import com.kubsu.timetable.presentation.subscription.list.SubListSideEffect
import com.kubsu.timetable.presentation.subscription.list.SubListState
import com.kubsu.timetable.presentation.subscription.list.SubListSubscription

fun subscriptionListFeature() = IosConnector(
    iosKodein.instanceDep<SubListState?, Feature<SubListAction, SubListSideEffect, SubListState, SubListSubscription>>(
        null
    )
)