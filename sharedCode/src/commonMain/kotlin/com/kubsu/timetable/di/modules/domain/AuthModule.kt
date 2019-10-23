package com.kubsu.timetable.di.modules.domain

import com.kubsu.timetable.data.db.MyDatabase
import com.kubsu.timetable.data.gateway.AuthGatewayImpl
import com.kubsu.timetable.domain.interactor.auth.AuthGateway
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.domain.interactor.auth.AuthInteractorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val authDomainModule = Kodein.Module("auth_domain") {
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
            userStorage = instance(),
            sessionStorage = instance(),
            tokenStorage = instance()
        )
    }
}