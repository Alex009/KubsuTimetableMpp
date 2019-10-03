package com.kubsu.timetable.di.modules.data

import com.kubsu.timetable.data.storage.maininfo.MainInfoStorage
import com.kubsu.timetable.data.storage.maininfo.MainInfoStorageImpl
import com.kubsu.timetable.data.storage.user.UserStorage
import com.kubsu.timetable.data.storage.user.UserStorageImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import platform.createSettingsFactory

internal val storageModule = Kodein.Module("storage") {
    bind() from singleton { createSettingsFactory(instance()) }
    bind<MainInfoStorage>() with singleton { MainInfoStorageImpl(instance()) }
    bind<UserStorage>() with singleton { UserStorageImpl(instance()) }
}