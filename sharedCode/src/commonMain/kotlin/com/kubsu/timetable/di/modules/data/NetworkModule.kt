package com.kubsu.timetable.di.modules.data

import com.kubsu.timetable.data.network.NetworkClient
import com.kubsu.timetable.data.network.NetworkClientImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val networkModule = Kodein.Module("network") {
    bind<NetworkClient>() with singleton {
        NetworkClientImpl(engine = instance())
    }
}