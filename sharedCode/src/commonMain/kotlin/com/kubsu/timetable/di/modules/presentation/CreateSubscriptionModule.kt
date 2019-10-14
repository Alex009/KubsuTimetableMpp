package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.bindGeneric
import com.kubsu.timetable.presentation.subscription.create.*
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val createSubscriptionPresentationModule =
    Kodein.Module("create_subscription_presentation") {
        bindGeneric<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
            TeaFeature(
                initialState = State(
                    progress = false,
                    facultyList = emptyList(),
                    occupationList = emptyList(),
                    groupList = emptyList(),
                    subgroupList = emptyList(),
                    selectedFaculty = null,
                    selectedOccupation = null,
                    selectedGroup = null,
                    selectedSubgroup = null
                ),
                update = createSubscriptionUpdater,
                effectHandler = CreateSubscriptionEffectHandler(instance()),
                onError = null
            )
        }
    }