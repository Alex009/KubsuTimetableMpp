package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.bindGeneric
import com.kubsu.timetable.presentation.subscription.list.*
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val subscriptionListPresentationModule = Kodein.Module("subscription_list_presentation") {
    bindGeneric<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature(
            initialState = State(
                progress = false,
                subscriptionList = emptyList()
            ),
            update = subscriptionListUpdater,
            effectHandler = SubscriptionListEffectHandler(instance()),
            onError = null
        )
    }
}