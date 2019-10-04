package com.kubsu.timetable.data.network.client.subscription.control

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto

interface ControlSubscriptionNetworkClient {
    suspend fun selectSubscriptionsForUser(): Either<NetworkFailure, List<SubscriptionNetworkDto>>
    suspend fun selectSubscriptionById(id: Int): Either<NetworkFailure, SubscriptionNetworkDto>
    suspend fun update(subscription: SubscriptionNetworkDto): Either<NetworkFailure, Unit>
    suspend fun deleteSubscription(id: Int): Either<NetworkFailure, Unit>
}