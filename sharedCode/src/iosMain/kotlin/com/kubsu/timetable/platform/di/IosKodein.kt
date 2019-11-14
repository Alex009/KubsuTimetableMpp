package com.kubsu.timetable.platform.di

import com.kubsu.timetable.platform.PlatformArgs
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val iosModule = Kodein {
    extend(iosCommonKodein, copy = Copy.All)

    bind() from singleton { PlatformArgs() }
}