package com.kubsu.timetable.di.modules.data

import com.kubsu.timetable.domain.validator.AuthValidator
import com.kubsu.timetable.domain.validator.AuthValidatorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

internal val validatorModule = Kodein.Module("validator") {
    bind<AuthValidator>() with singleton { AuthValidatorImpl() }
}