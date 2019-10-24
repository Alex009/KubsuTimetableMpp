package com.kubsu.timetable.di

import com.kubsu.timetable.di.modules.data.dbModule
import com.kubsu.timetable.di.modules.data.networkModule
import com.kubsu.timetable.di.modules.data.storageModule
import com.kubsu.timetable.di.modules.domain.*
import com.kubsu.timetable.di.modules.presentation.*
import org.kodein.di.Kodein

internal val mppCommonKodeinModule = Kodein.Module("mpp_common_module") {
    // presentation
    importAll(
        appPresentationModule,
        splashPresentationModule,
        settingsPresentationModule,
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