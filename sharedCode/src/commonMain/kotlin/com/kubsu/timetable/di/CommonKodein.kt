package com.kubsu.timetable.di

import com.kubsu.timetable.di.modules.data.dbModule
import com.kubsu.timetable.di.modules.data.networkModule
import com.kubsu.timetable.di.modules.data.storageModule
import com.kubsu.timetable.di.modules.domain.*
import com.kubsu.timetable.di.modules.presentation.*
import com.kubsu.timetable.domain.syncronizer.InformationSynchronizer
import com.kubsu.timetable.domain.syncronizer.InformationSynchronizerImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val mppCommonKodeinModule = Kodein.Module("mpp_common_module") {
    // presentation
    importAll(
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

    bind<InformationSynchronizer>() with singleton {
        InformationSynchronizerImpl(
            userInteractor = instance(),
            syncMixinInteractor = instance(),
            platformArgs = instance()
        )
    }
}