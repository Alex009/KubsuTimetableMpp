package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.extensions.instanceGeneric
import com.kubsu.timetable.fragments.bottomnav.settings.SettingsFragment
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val settingsViewModule = Kodein.Module("settings_view_module") {
    bind() from provider {
        SettingsFragment(featureFactory = ::instanceGeneric)
    }
}