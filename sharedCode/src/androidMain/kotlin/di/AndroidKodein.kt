package di

import android.app.Application
import android.content.Context
import com.kubsu.timetable.data.storage.maininfo.MainInfoStorage
import com.kubsu.timetable.data.storage.user.UserStorage
import com.kubsu.timetable.di.commonKodein
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import platform.PlatformDriverArgs
import platform.createDriver
import storage.MainInfoStorageImpl
import storage.UserStorageImpl

fun androidKodein(application: Application): Kodein.Module =
    Kodein.Module("android_module") {
        import(commonKodein)

        bind<Context>() with singleton { application.applicationContext }
        bind() from singleton { createDriver(PlatformDriverArgs(instance())) }

        bind<MainInfoStorage>() with singleton { MainInfoStorageImpl(instance()) }
        bind<UserStorage>() with singleton { UserStorageImpl(instance()) }
    }