package di

import android.app.Application
import android.content.Context
import com.kubsu.timetable.di.commonKodein
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import platform.PlatformArgs

fun androidKodein(application: Application): Kodein.Module =
    Kodein.Module("android_module") {
        import(commonKodein)

        bind<Context>() with singleton { application.applicationContext }
        bind() from singleton { PlatformArgs(instance()) }
    }