package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.subscription.create.CreateSub
import com.kubsu.timetable.presentation.subscription.create.CreateSubscriptionEffectHandler
import com.kubsu.timetable.presentation.subscription.create.createSubscriptionUpdater
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val createSubscriptionPresentationModule =
    Kodein.Module("create_subscription_presentation") {
        bindGeneric<Feature<CreateSub.Action, CreateSub.SideEffect, CreateSub.State, CreateSub.Subscription>>() with factory { defaultState: CreateSub.State? ->
            TeaFeature(
                initialState = defaultState ?: CreateSub.State(
                    progress = false,
                    facultyList = emptyList(),
                    occupationList = emptyList(),
                    groupList = emptyList(),
                    subgroupList = emptyList(),
                    nameHint = null,
                    selectedFaculty = null,
                    selectedOccupation = null,
                    selectedGroup = null,
                    selectedSubgroup = null
                ),
                updater = createSubscriptionUpdater,
                effectHandler = CreateSubscriptionEffectHandler(instance(), instance()),
                onError = null
            )
        }
    }