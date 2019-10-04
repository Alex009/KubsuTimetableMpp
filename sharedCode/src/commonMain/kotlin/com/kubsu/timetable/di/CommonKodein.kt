package com.kubsu.timetable.di

import com.kubsu.timetable.di.modules.data.dbModule
import com.kubsu.timetable.di.modules.data.networkModule
import com.kubsu.timetable.di.modules.data.storageModule
import com.kubsu.timetable.di.modules.domain.*
import org.kodein.di.Kodein

internal val commonKodein = Kodein.Module("common") {
    // domain
    import(userModule)
    import(timetableModule)
    import(subscriptionModule)
    import(authModule)
    import(syncMixinModule)

    // data
    import(networkModule)
    import(storageModule)
    import(dbModule)
}