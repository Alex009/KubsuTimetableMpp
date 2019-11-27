package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.subscription.list.SubList
import com.kubsu.timetable.presentation.subscription.list.SubscriptionListEffectHandler
import com.kubsu.timetable.presentation.subscription.list.subscriptionListUpdater
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val subscriptionListPresentationModule = Kodein.Module("subscription_list_presentation") {
    bindGeneric<Feature<SubList.Action, SubList.SideEffect, SubList.State, SubList.Subscription>>() with factory { defaultState: SubList.State? ->
        TeaFeature(
            initialState = defaultState ?: SubList.State(
                progress = false,
                subscriptionList = emptyList()
            ),
            updater = subscriptionListUpdater,
            effectHandler = SubscriptionListEffectHandler(instance(), instance()),
            onError = null
        )
    }
}