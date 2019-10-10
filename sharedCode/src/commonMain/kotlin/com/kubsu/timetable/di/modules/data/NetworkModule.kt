package com.kubsu.timetable.di.modules.data

import com.kubsu.timetable.data.network.client.subscription.SubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.subscription.SubscriptionNetworkClientImpl
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClient
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClientImpl
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClientImpl
import com.kubsu.timetable.data.network.client.update.UpdateDataNetworkClient
import com.kubsu.timetable.data.network.client.update.UpdateDataNetworkClientImpl
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClientImpl
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.NetworkSenderImpl
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val networkModule = Kodein.Module("network") {
    @UseExperimental(UnstableDefault::class)
    bind() from singleton { Json(JsonConfiguration.Default.copy(strictMode = false)) }

    bind<NetworkSender>() with singleton {
        NetworkSenderImpl(instance(), instance())
    }

    // subscription
    bind<SubscriptionNetworkClient>() with singleton {
        SubscriptionNetworkClientImpl(instance(), instance())
    }
    bind<UniversityDataNetworkClient>() with singleton {
        UniversityDataNetworkClientImpl(instance())
    }

    // timetable
    bind<TimetableNetworkClient>() with singleton {
        TimetableNetworkClientImpl(instance())
    }

    // update data
    bind<UpdateDataNetworkClient>() with singleton {
        UpdateDataNetworkClientImpl(instance())
    }

    // user info
    bind<UserInfoNetworkClient>() with singleton {
        UserInfoNetworkClientImpl(instance(), instance())
    }
}