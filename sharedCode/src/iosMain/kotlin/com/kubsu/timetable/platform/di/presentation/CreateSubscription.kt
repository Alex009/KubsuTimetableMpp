package com.kubsu.timetable.platform.di.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.IosConnector
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.platform.di.iosKodein
import com.kubsu.timetable.presentation.subscription.create.SubCreateAction
import com.kubsu.timetable.presentation.subscription.create.SubCreateSideEffect
import com.kubsu.timetable.presentation.subscription.create.SubCreateState
import com.kubsu.timetable.presentation.subscription.create.SubCreateSubscription

fun createSubscriptionFeature() = IosConnector(
    iosKodein.instanceDep<SubCreateState?, Feature<SubCreateAction, SubCreateSideEffect, SubCreateState, SubCreateSubscription>>(
        null
    )
)