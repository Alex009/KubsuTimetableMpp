package com.kubsu.timetable.di.modules.data

import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorage
import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorageImpl
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.info.UserStorageImpl
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.session.SessionStorageImpl
import com.kubsu.timetable.data.storage.user.token.TokenStorage
import com.kubsu.timetable.data.storage.user.token.TokenStorageImpl
import com.kubsu.timetable.platform.createSettingsFactory
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val storageModule = Kodein.Module("storage") {
    bind() from singleton { createSettingsFactory(instance()) }
    bind<UserStorage>() with singleton { UserStorageImpl(instance()) }
    bind<SessionStorage>() with singleton { SessionStorageImpl(instance()) }
    bind<DisplayedSubscriptionStorage>() with singleton {
        DisplayedSubscriptionStorageImpl(instance())
    }
    bind<TokenStorage>() with singleton {
        TokenStorageImpl(instance())
    }
}