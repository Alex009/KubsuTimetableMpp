package platform.di

import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import platform.PlatformArgs

val iosModule = Kodein {
    extend(iosCommonKodein, copy = Copy.All)

    bind() from singleton { PlatformArgs() }
}