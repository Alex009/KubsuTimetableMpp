package com.kubsu.timetable.di.modules.view

import com.egroden.teaco.StateParser
import com.kubsu.timetable.fragments.subscription.create.CreateSubscriptionFragment
import com.kubsu.timetable.instanceGeneric
import com.kubsu.timetable.presentation.subscription.create.State
import kotlinx.serialization.json.Json
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider

val createSubscriptionViewModule = Kodein.Module("create_subscription_view_module") {
    bind() from provider {
        val json: Json = instance()
        CreateSubscriptionFragment(
            teaFeature = instanceGeneric(),
            stateParser = StateParser(
                first = { state -> json.stringify(State.serializer(), state) },
                second = { string -> json.parse(State.serializer(), string) }
            )
        )
    }
}