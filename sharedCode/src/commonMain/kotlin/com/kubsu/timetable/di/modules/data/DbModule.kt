package com.kubsu.timetable.di.modules.data

import com.kubsu.timetable.data.db.MyDatabase
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import platform.createDriver

internal val dbModule = Kodein.Module("db") {
    bind<MyDatabase>() with singleton { MyDatabase(instance()) }
    bind() from singleton { createDriver(instance()) }
}