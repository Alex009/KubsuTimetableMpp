package com.kubsu.timetable.platform.di

import android.app.Application
import com.kubsu.timetable.di.mppCommonKodeinModule
import com.kubsu.timetable.platform.PlatformArgs
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun androidCommonKodein(application: Application) = Kodein {
    import(mppCommonKodeinModule)

    bind<HttpClientEngine>() with singleton {
        Android.create {}
    }

    bind() from singleton { PlatformArgs(application.applicationContext) }
}