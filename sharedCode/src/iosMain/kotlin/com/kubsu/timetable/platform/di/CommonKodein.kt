package com.kubsu.timetable.platform.di

import com.kubsu.timetable.di.mppCommonKodeinModule
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.ios.Ios
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val iosCommonKodein = Kodein {
    import(mppCommonKodeinModule)

    bind<HttpClientEngine>() with singleton {
        Ios.create {}
    }
}