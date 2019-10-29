package com.kubsu.timetable.data.network.client.subscription

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto

interface SubscriptionNetworkClient {
    suspend fun createSubscription(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionNetworkDto>

    suspend fun selectSubscriptionsForUser(): Either<DataFailure, List<SubscriptionNetworkDto>>

    suspend fun selectSubscriptionById(id: Int): Either<DataFailure, SubscriptionNetworkDto>

    suspend fun update(
        subscription: SubscriptionNetworkDto
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit>

    suspend fun deleteSubscription(
        id: Int
    ): Either<DataFailure, Unit>
}