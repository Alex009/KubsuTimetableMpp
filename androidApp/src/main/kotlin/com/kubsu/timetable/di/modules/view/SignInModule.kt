package com.kubsu.timetable.di.modules.view

import com.kubsu.timetable.extensions.instanceGeneric
import com.kubsu.timetable.fragments.signin.SignInFragment
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val signInViewModule = Kodein.Module("sign_in_view_module") {
    bind() from provider {
        SignInFragment(teaFeature = instanceGeneric())
    }
}