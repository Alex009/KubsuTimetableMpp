package com.kubsu.timetable.di.modules.domain

import com.kubsu.timetable.data.db.MyDatabase
import com.kubsu.timetable.data.gateway.AppInfoGatewayImpl
import com.kubsu.timetable.data.gateway.TimetableGatewayImpl
import com.kubsu.timetable.domain.interactor.timetable.AppInfoGateway
import com.kubsu.timetable.domain.interactor.timetable.TimetableGateway
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractor
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val timetableDomainModule = Kodein.Module("timetable_domain") {
    bind<TimetableInteractor>() with singleton {
        TimetableInteractorImpl(instance())
    }
    bind<TimetableGateway>() with singleton {
        val db = instance<MyDatabase>()
        TimetableGatewayImpl(
            timetableQueries = db.timetableQueries,
            classQueries = db.classQueries,
            classTimeQueries = db.classTimeQueries,
            lecturerQueries = db.lecturerQueries,
            universityInfoQueries = db.universityInfoQueries
        )
    }
    bind<AppInfoGateway>() with singleton {
        val db = instance<MyDatabase>()
        AppInfoGatewayImpl(
            classQueries = db.classQueries,
            classTimeQueries = db.classTimeQueries,
            lecturerQueries = db.lecturerQueries,
            subscriptionQueries = db.subscriptionQueries,
            timetableQueries = db.timetableQueries,
            universityInfoQueries = db.universityInfoQueries,
            universityDataNetworkClient = instance(),
            subscriptionNetworkClient = instance(),
            timetableNetworkClient = instance()
        )
    }
}