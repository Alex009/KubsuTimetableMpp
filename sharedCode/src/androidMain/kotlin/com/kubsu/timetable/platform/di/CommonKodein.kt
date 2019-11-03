package com.kubsu.timetable.platform.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val androidCommonKodein = Kodein {
    import(com.kubsu.timetable.di.mppCommonKodeinModule)

    bind<HttpClientEngine>() with singleton {
        Android.create {}
    }
}