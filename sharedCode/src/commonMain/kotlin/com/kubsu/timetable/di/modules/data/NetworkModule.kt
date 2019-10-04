package com.kubsu.timetable.di.modules.data

import com.kubsu.timetable.data.network.client.subscription.control.ControlSubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.subscription.control.ControlSubscriptionNetworkClientImpl
import com.kubsu.timetable.data.network.client.subscription.create.CreateSubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.subscription.create.CreateSubscriptionNetworkClientImpl
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClient
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClientImpl
import com.kubsu.timetable.data.network.client.update.UpdateDataNetworkClient
import com.kubsu.timetable.data.network.client.update.UpdateDataNetworkClientImpl
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClientImpl
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.NetworkSenderImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val networkModule = Kodein.Module("network") {
    bind<NetworkSender>() with singleton { NetworkSenderImpl(instance()) }

    // subscription
    bind<ControlSubscriptionNetworkClient>() with singleton {
        ControlSubscriptionNetworkClientImpl(instance())
    }
    bind<CreateSubscriptionNetworkClient>() with singleton {
        CreateSubscriptionNetworkClientImpl(instance())
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
        UserInfoNetworkClientImpl(instance())
    }
}