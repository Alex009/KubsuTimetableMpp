package com.kubsu.timetable.di.modules.view

import com.egroden.teaco.StateParser
import com.kubsu.timetable.fragments.registration.RegistrationFragment
import com.kubsu.timetable.instanceGeneric
import com.kubsu.timetable.presentation.registration.State
import kotlinx.serialization.json.Json
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider

val registrationViewModule = Kodein.Module("registration_view_module") {
    bind() from provider {
        val json: Json = instance()
        RegistrationFragment(
            teaFeature = instanceGeneric(),
            stateParser = StateParser(
                first = { state -> json.stringify(State.serializer(), state) },
                second = { string -> json.parse(State.serializer(), string) }
            )
        )
    }
}