package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.extensions.instanceGeneric
import com.kubsu.timetable.fragments.subscription.create.CreateSubscriptionFragment
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val createSubscriptionViewModule = Kodein.Module("create_subscription_view_module") {
    bind() from provider {
        CreateSubscriptionFragment(featureFactory = ::instanceGeneric)
    }
}