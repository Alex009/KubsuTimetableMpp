package com.kubsu.timetable.di.modules.domain

import com.kubsu.timetable.data.db.MyDatabase
import com.kubsu.timetable.data.gateway.SubscriptionGatewayImpl
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionGateway
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractorImpl
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal val subscriptionDomainModule = Kodein.Module("subscription_domain") {
    bind<SubscriptionInteractor>() with singleton {
        SubscriptionInteractorImpl(instance(), instance(), instance())
    }
    bind<SubscriptionGateway>() with singleton {
        val db = instance<MyDatabase>()
        SubscriptionGatewayImpl(db.subscriptionQueries, instance(), instance())
    }
}