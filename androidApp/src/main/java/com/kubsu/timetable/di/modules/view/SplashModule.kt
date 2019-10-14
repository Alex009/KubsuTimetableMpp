package com.kubsu.timetable.di.modules.view

import com.egroden.teaco.StateParser
import com.kubsu.timetable.fragments.splash.SplashFragment
import com.kubsu.timetable.instanceGeneric
import com.kubsu.timetable.presentation.splash.State
import kotlinx.serialization.json.Json
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider

val splashViewModule = Kodein.Module("splash_view_module") {
    bind() from provider {
        val json: Json = instance()
        SplashFragment(
            teaFeature = instanceGeneric(),
            stateParser = StateParser(
                first = { state -> json.stringify(State.serializer(), state) },
                second = { string -> json.parse(State.serializer(), string) }
            )
        )
    }
}