package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.fragments.subscription.create.CreateSubscriptionFragment
import com.kubsu.timetable.instanceGeneric
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val createSubscriptionViewModule = Kodein.Module("create_subscription_view_module") {
    bind() from provider {
        CreateSubscriptionFragment(teaFeature = instanceGeneric())
    }
}