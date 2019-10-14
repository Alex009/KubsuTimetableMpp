package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.Updater
import com.kubsu.timetable.presentation.subscription.create.*
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val createSubscriptionPresentationModule =
    Kodein.Module("create_subscription_presentation") {
        bind<EffectHandler<SideEffect, Action>>() with singleton {
            CreateSubscriptionEffectHandler(instance())
        }
        bind<Updater<State, Action, Subscription, SideEffect>>() with singleton { createSubscriptionUpdater }
        bind<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
            TeaFeature<Action, SideEffect, State, Subscription>(
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
                update = instance(),
                effectHandler = instance(),
                onError = null
            )
        }
    }