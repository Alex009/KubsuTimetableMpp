package com.kubsu.timetable.di.modules.view

import com.egroden.teaco.StateParser
import com.kubsu.timetable.fragments.signin.SignInFragment
import com.kubsu.timetable.instanceGeneric
import com.kubsu.timetable.presentation.signin.State
import kotlinx.serialization.json.Json
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider

val signInViewModule = Kodein.Module("sign_in_view_module") {
    bind() from provider {
        val json: Json = instance()
        SignInFragment(
            teaFeature = instanceGeneric(),
            stateParser = StateParser(
                first = { state -> json.stringify(State.serializer(), state) },
                second = { string -> json.parse(State.serializer(), string) }
            )
        )
    }
}