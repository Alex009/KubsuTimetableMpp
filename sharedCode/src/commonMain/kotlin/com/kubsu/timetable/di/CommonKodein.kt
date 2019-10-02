package com.kubsu.timetable.di

import com.kubsu.timetable.data.db.MyDatabase
import com.kubsu.timetable.data.gateway.*
import com.kubsu.timetable.data.network.NetworkClient
import com.kubsu.timetable.data.network.NetworkClientImpl
import com.kubsu.timetable.data.storage.maininfo.MainInfoStorage
import com.kubsu.timetable.data.storage.maininfo.MainInfoStorageImpl
import com.kubsu.timetable.data.storage.user.UserStorage
import com.kubsu.timetable.data.storage.user.UserStorageImpl
import com.kubsu.timetable.domain.interactor.auth.AuthGateway
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.domain.interactor.auth.AuthInteractorImpl
import com.kubsu.timetable.domain.interactor.main.MainGateway
import com.kubsu.timetable.domain.interactor.main.MainInteractor
import com.kubsu.timetable.domain.interactor.main.MainInteractorImpl
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionGateway
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractorImpl
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractorImpl
import com.kubsu.timetable.domain.interactor.timetable.TimetableGateway
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractor
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractorImpl
import com.kubsu.timetable.domain.validator.AuthValidator
import com.kubsu.timetable.domain.validator.AuthValidatorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import platform.createDriver
import platform.createSettingsFactory

internal val commonKodein = Kodein.Module("common_module") {
    // Main
    bind<MainInteractor>() with singleton { MainInteractorImpl(instance()) }
    bind<MainGateway>() with singleton { MainGatewayImpl(instance(), instance(), instance()) }

    // Timetable
    bind<TimetableInteractor>() with singleton { TimetableInteractorImpl(instance()) }
    bind<TimetableGateway>() with singleton {
        val db = instance<MyDatabase>()
        TimetableGatewayImpl(
            timetableQueries = db.timetableQueries,
            classQueries = db.classQueries,
            classTimeQueries = db.classTimeQueries,
            lecturerQueries = db.lecturerQueries,
            networkClient = instance()
        )
    }

    // Subscription
    bind<SubscriptionInteractor>() with singleton {
        SubscriptionInteractorImpl(instance(), instance())
    }
    bind<SubscriptionGateway>() with singleton {
        val db = instance<MyDatabase>()
        SubscriptionGatewayImpl(db.subscriptionQueries, instance())
    }

    // Auth
    bind<AuthInteractor>() with singleton { AuthInteractorImpl(instance(), instance()) }
    bind<AuthGateway>() with singleton {
        val db = instance<MyDatabase>()
        AuthGatewayImpl(
            classQueries = db.classQueries,
            classTimeQueries = db.classTimeQueries,
            lecturerQueries = db.lecturerQueries,
            subscriptionQueries = db.subscriptionQueries,
            timetableQueries = db.timetableQueries,
            dataDiffQueries = db.dataDiffQueries,
            updatedEntityQueries = db.updatedEntityQueries,
            deletedEntityQueries = db.deletedEntityQueries,
            networkClient = instance(),
            userStorage = instance()
        )
    }

    // Sync mixin
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

    // Validator
    bind<AuthValidator>() with singleton { AuthValidatorImpl() }

    // Settings factory
    bind() from singleton { createSettingsFactory(instance()) }

    // Db
    bind<MyDatabase>() with singleton { MyDatabase(instance()) }
    bind() from singleton { createDriver(instance()) }

    // Storage
    bind<MainInfoStorage>() with singleton { MainInfoStorageImpl(instance()) }
    bind<UserStorage>() with singleton { UserStorageImpl(instance()) }

    // Network
    bind<NetworkClient>() with singleton {
        NetworkClientImpl(engine = instance())
    }
}