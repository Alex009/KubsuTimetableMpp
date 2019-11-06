package com.kubsu.timetable.di.modules.domain

import com.kubsu.timetable.data.db.MyDatabase
import com.kubsu.timetable.data.gateway.AppInfoGatewayImpl
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoGateway
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoInteractor
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoInteractorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val appInfoDomainModule = Kodein.Module("app_info_domain") {
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

    bind<AppInfoInteractor>() with singleton {
        AppInfoInteractorImpl(
            appInfoGateway = instance(),
            userInfoGateway = instance()
        )
    }
}