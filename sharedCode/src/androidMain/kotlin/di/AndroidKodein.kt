package di

import android.app.Application
import com.kubsu.timetable.di.commonKodein
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import platform.PlatformArgs

fun androidModule(application: Application): Kodein.Module =
    Kodein.Module("android") {
        import(commonKodein)

        bind() from singleton { PlatformArgs(application.applicationContext) }

        bind<HttpClientEngine>() with singleton {
            Android.create {}
        }
    }