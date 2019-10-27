package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.extensions.instanceGeneric
import com.kubsu.timetable.fragments.bottomnav.subscription.list.SubscriptionListFragment
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val subscriptionListViewModule = Kodein.Module("subscription_list_view_module") {
    bind() from provider {
        SubscriptionListFragment(featureFactory = ::instanceGeneric)
    }
}