package com.kubsu.timetable.di.modules.domain

import com.kubsu.timetable.data.gateway.UserInfoGatewayImpl
import com.kubsu.timetable.domain.interactor.main.UserInfoGateway
import com.kubsu.timetable.domain.interactor.main.UserInteractor
import com.kubsu.timetable.domain.interactor.main.UserInteractorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val userModule = Kodein.Module("main") {
    bind<UserInteractor>() with singleton { UserInteractorImpl(instance()) }
    bind<UserInfoGateway>() with singleton { UserInfoGatewayImpl(instance(), instance()) }
}