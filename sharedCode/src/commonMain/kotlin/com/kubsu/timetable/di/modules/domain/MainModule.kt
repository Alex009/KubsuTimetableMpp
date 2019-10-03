package com.kubsu.timetable.di.modules.domain

import com.kubsu.timetable.data.gateway.MainGatewayImpl
import com.kubsu.timetable.domain.interactor.main.MainGateway
import com.kubsu.timetable.domain.interactor.main.MainInteractor
import com.kubsu.timetable.domain.interactor.main.MainInteractorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val mainModule = Kodein.Module("main") {
    bind<MainInteractor>() with singleton { MainInteractorImpl(instance()) }
    bind<MainGateway>() with singleton { MainGatewayImpl(instance(), instance(), instance()) }
}