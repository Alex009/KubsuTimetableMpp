package com.kubsu.timetable.di

import com.kubsu.timetable.di.modules.data.dbModule
import com.kubsu.timetable.di.modules.data.networkModule
import com.kubsu.timetable.di.modules.data.storageModule
import com.kubsu.timetable.di.modules.domain.*
import com.kubsu.timetable.di.modules.presentation.*
import org.kodein.di.Kodein

internal val commonKodein = Kodein.Module("common_module") {
    // presentation
    importAll(
        splashPresentationModule,
        signInPresentationModule,
        registrationPresentationModule,
        createSubscriptionPresentationModule,
        subscriptionListPresentationModule,
        timetablePresentationModule
    )

    // domain
    importAll(
        userDomainModule,
        timetableDomainModule,
        subscriptionDomainModule,
        authDomainModule,
        syncMixinDomainModule
    )

    // data
    importAll(
        networkModule,
        storageModule,
        dbModule
    )
}