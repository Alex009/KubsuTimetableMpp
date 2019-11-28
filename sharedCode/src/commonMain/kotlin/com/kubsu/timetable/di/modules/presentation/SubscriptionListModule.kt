package com.kubsu.timetable.di.modules.presentation

import com.egroden.teaco.Feature
import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.extensions.bindGeneric
import com.kubsu.timetable.presentation.subscription.list.*
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

internal val subscriptionListPresentationModule = Kodein.Module("subscription_list_presentation") {
    bindGeneric<Feature<SubListAction, SubListSideEffect, SubListState, SubListSubscription>>() with factory { defaultState: SubListState? ->
        TeaFeature(
            initialState = defaultState ?: SubListState(
                progress = false,
                subscriptionList = emptyList()
            ),
            updater = subscriptionListUpdater,
            effectHandler = SubscriptionListEffectHandler(instance(), instance()),
            onError = null
        )
    }
}