package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.extensions.instanceDep
import com.kubsu.timetable.presentation.subscription.create.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val createSubscriptionPresentationModule =
    Kodein.Module("create_subscription_presentation") {
        bindGeneric<Feature<Action, SideEffect, State, Subscription>>() with factory { defaultState: State? ->
            TeaFeature(
                initialState = defaultState ?: State(
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
                update = createSubscriptionUpdater,
                effectHandler = CreateSubscriptionEffectHandler(instance(), instance()),
                onError = null
            )
        }
    }

fun KodeinAware.getCreateSubscriptionFeature(defaultState: State? = null): Feature<Action, SideEffect, State, Subscription> =
    instanceDep(defaultState)