package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.Updater
import com.kubsu.timetable.presentation.subscription.list.*
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val subscriptionListPresentationModule = Kodein.Module("subscription_list_presentation") {
    bind<EffectHandler<SideEffect, Action>>() with singleton {
        SubscriptionListEffectHandler(instance())
    }
    bind<Updater<State, Action, Subscription, SideEffect>>() with singleton { subscriptionListUpdater }
    bind<TeaFeature<Action, SideEffect, State, Subscription>>() with singleton {
        TeaFeature<Action, SideEffect, State, Subscription>(
            initialState = State(
                progress = false,
                subscriptionList = emptyList()
            ),
            update = instance(),
            effectHandler = instance(),
            onError = null
        )
    }
}