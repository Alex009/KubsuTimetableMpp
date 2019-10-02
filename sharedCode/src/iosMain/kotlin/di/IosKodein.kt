package di

import com.kubsu.timetable.di.commonKodein
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import platform.PlatformArgs

val iosKodein = Kodein.Module("ios_kodein") {
    import(commonKodein)

    bind() from singleton { PlatformArgs() }
}