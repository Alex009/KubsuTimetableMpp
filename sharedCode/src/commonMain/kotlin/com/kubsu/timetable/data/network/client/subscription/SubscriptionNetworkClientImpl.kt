package com.kubsu.timetable.data.network.client.subscription

import com.egroden.teaco.Either
import com.egroden.teaco.mapLeft
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Failure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.data.network.client.subscription.incorrectdata.SubscriptionIncorrectData
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import com.kubsu.timetable.data.storage.user.session.SessionDto
import com.kubsu.timetable.extensions.addSessionKey
import com.kubsu.timetable.extensions.jsonContent
import com.kubsu.timetable.extensions.toJson
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import kotlinx.serialization.json.Json

class SubscriptionNetworkClientImpl(
    private val networkSender: NetworkSender,
    private val json: Json
) : SubscriptionNetworkClient {
    override suspend fun createSubscription(
        session: SessionDto,
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionNetworkDto> =
        with(networkSender) {
            handle {
                post<SubscriptionNetworkDto>("$baseUrl/api/$apiVersion/subscriptions/") {
                    addSessionKey(session)
                    body = jsonContent(
                        "subgroup" to subgroupId.toJson(),
                        "title" to subscriptionName.toJson(),
                        "is_main" to isMain.toJson()
                    )
                }
            }.mapLeft { failure ->
                if (failure is ServerFailure.Response && failure.code in 400..401)
                    if (failure.code == 401)
                        RequestFailure(DataFailure.NotAuthenticated(failure.body))
                    else
                        parseCreateFail(failure)
                else
                    RequestFailure(toNetworkFail(failure))
            }
        }

    private fun parseCreateFail(
        response: ServerFailure.Response
    ): RequestFailure<List<SubscriptionFail>> {
        val incorrectData = json.parse(SubscriptionIncorrectData.serializer(), response.body)

        val titleFailureList = mapTitleFailureList(incorrectData, response)
        val subgroupFailureList = mapSubgroupFailureList(incorrectData, response)
        val otherFailureList = mapOtherFailureList(incorrectData, response)

        val failList = titleFailureList + subgroupFailureList + otherFailureList
        return RequestFailure(
            domain = failList.filterIsInstance<SubscriptionFail>(),
            data = failList.filterIsInstance<DataFailure>()
        )
    }

    private fun mapTitleFailureList(
        incorrectData: SubscriptionIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.title.map { failure ->
            when (failure) {
                "max_length" -> SubscriptionFail.TooLongTitle
                "required" -> SubscriptionFail.RequiredTitle
                else -> DataFailure.UnknownResponse(response.code, response.body)
            }
        }

    private fun mapSubgroupFailureList(
        incorrectData: SubscriptionIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.subgroup.map {
            DataFailure.UnknownResponse(response.code, response.body)
        }

    private fun mapOtherFailureList(
        incorrectData: SubscriptionIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.nonFieldFailures.map { failure ->
            when (failure) {
                "unique" -> SubscriptionFail.SubscriptionAlreadyExists
                else -> DataFailure.UnknownResponse(response.code, response.body)
            }
        }

    override suspend fun selectSubscriptionsForUser(
        session: SessionDto
    ): Either<DataFailure, List<SubscriptionNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<SubscriptionNetworkDto>>("$baseUrl/api/$apiVersion/subscriptions/") {
                    addSessionKey(session)
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }

    override suspend fun selectSubscriptionById(id: Int): Either<DataFailure, SubscriptionNetworkDto> =
        with(networkSender) {
            handle {
                get<SubscriptionNetworkDto>("$baseUrl/api/$apiVersion/subscriptions/$id/")
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }

    override suspend fun update(
        session: SessionDto,
        subscription: SubscriptionNetworkDto
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> =
        with(networkSender) {
            handle {
                patch<Unit>("$baseUrl/api/$apiVersion/subscriptions/${subscription.id}/") {
                    addSessionKey(session)
                    body = jsonContent(
                        "subgroup" to subscription.subgroup.toJson(),
                        "name" to subscription.title.toJson(),
                        "is_main" to subscription.isMain.toJson()
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && (it.code == 400 || it.code == 401))
                    if (it.code == 401)
                        RequestFailure(DataFailure.NotAuthenticated(it.body))
                    else
                        parseCreateFail(it)
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    override suspend fun deleteSubscription(
        session: SessionDto,
        id: Int
    ): Either<DataFailure, Unit> =
        with(networkSender) {
            handle {
                delete<Unit>("$baseUrl/api/$apiVersion/subscriptions/$id/") {
                    addSessionKey(session)
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }
}