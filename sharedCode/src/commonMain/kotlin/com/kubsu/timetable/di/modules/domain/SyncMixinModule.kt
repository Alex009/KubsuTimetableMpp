package com.kubsu.timetable.di.modules.domain

import com.kubsu.timetable.data.db.MyDatabase
import com.kubsu.timetable.data.gateway.SyncMixinGatewayImpl
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val syncMixinModule = Kodein.Module("syncMixin") {
    bind<SyncMixinInteractor>() with singleton { SyncMixinInteractorImpl(instance(), instance()) }
    bind<SyncMixinGateway>() with singleton {
        val db = instance<MyDatabase>()
        SyncMixinGatewayImpl(
            classQueries = db.classQueries,
            lecturerQueries = db.lecturerQueries,
            subscriptionQueries = db.subscriptionQueries,
            timetableQueries = db.timetableQueries,
            dataDiffQueries = db.dataDiffQueries,
            updatedEntityQueries = db.updatedEntityQueries,
            deletedEntityQueries = db.deletedEntityQueries,
            networkClient = instance(),
            mainGateway = instance()
        )
    }
}