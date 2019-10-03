package di

import com.kubsu.timetable.di.commonKodein
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.ios.Ios
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import platform.PlatformArgs

val iosModule = Kodein.Module("ios") {
    import(commonKodein)

    bind() from singleton { PlatformArgs() }

    bind<HttpClientEngine>() with singleton {
        Ios.create {}
    }
}