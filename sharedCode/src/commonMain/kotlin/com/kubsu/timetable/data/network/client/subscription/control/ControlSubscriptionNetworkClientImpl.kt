package com.kubsu.timetable.data.network.client.subscription.control

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto

class ControlSubscriptionNetworkClientImpl : ControlSubscriptionNetworkClient {
    override suspend fun selectSubscriptionsForUser(): Either<NetworkFailure, List<SubscriptionNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectSubscriptionById(id: Int): Either<NetworkFailure, SubscriptionNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(subscription: SubscriptionNetworkDto): Either<NetworkFailure, SubscriptionNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteSubscription(id: Int): Either<NetworkFailure, SubscriptionNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}