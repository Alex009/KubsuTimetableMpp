package com.kubsu.timetable.data.network.client.subscription.control

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.http.Parameters

class ControlSubscriptionNetworkClientImpl(
    private val networkSender: NetworkSender
) : ControlSubscriptionNetworkClient {
    override suspend fun selectSubscriptionsForUser(): Either<NetworkFailure, List<SubscriptionNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<SubscriptionNetworkDto>>("$baseUrl/api/$apiVersion/subscriptions/")
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectSubscriptionById(id: Int): Either<NetworkFailure, SubscriptionNetworkDto> =
        with(networkSender) {
            handle {
                get<SubscriptionNetworkDto>("$baseUrl/api/$apiVersion/subscriptions/$id/")
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun update(subscription: SubscriptionNetworkDto): Either<NetworkFailure, Unit> =
        with(networkSender) {
            handle {
                patch<Unit>("$baseUrl/api/$apiVersion/subscriptions/${subscription.id}/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("subgroup", subscription.subgroup.toString())
                            append("name", subscription.title)
                            append("is_main", subscription.isMain.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun deleteSubscription(id: Int): Either<NetworkFailure, Unit> =
        with(networkSender) {
            handle {
                delete<Unit>("$baseUrl/api/$apiVersion/subscriptions/$id/")
            }.mapLeft(::toNetworkFail)
        }
}