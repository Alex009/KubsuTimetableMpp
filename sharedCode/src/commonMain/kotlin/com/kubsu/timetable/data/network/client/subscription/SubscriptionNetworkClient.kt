package com.kubsu.timetable.data.network.client.subscription

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.storage.user.session.Session

interface SubscriptionNetworkClient {
    suspend fun createSubscription(
        session: Session,
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionNetworkDto>

    suspend fun selectSubscriptionsForUser(
        session: Session
    ): Either<DataFailure, List<SubscriptionNetworkDto>>

    suspend fun selectSubscriptionById(id: Int): Either<DataFailure, SubscriptionNetworkDto>

    suspend fun update(
        session: Session,
        subscription: SubscriptionNetworkDto
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit>

    suspend fun deleteSubscription(
        session: Session,
        id: Int
    ): Either<DataFailure, Unit>
}