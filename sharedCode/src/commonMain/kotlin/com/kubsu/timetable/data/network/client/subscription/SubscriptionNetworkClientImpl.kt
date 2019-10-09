package com.kubsu.timetable.data.network.client.subscription

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.data.network.client.subscription.incorrectdata.SubscriptionIncorrectData
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.http.Parameters
import kotlinx.serialization.json.Json

class SubscriptionNetworkClientImpl(
    private val networkSender: NetworkSender,
    private val json: Json
) : SubscriptionNetworkClient {
    override suspend fun createSubscription(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionNetworkDto> =
        with(networkSender) {
            handle {
                post<SubscriptionNetworkDto>("$baseUrl/api/$apiVersion/subscriptions/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("subgroup", subgroupId.toString())
                            append("title", subscriptionName)
                            append("is_main", isMain.toString())
                        }
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

    private fun parseCreateFail(
        response: ServerFailure.Response
    ): RequestFailure<List<SubscriptionFail>> {
        val incorrectData = json.parse(SubscriptionIncorrectData.serializer(), response.body)
        return handleCreateFail(
            responseCode = response.code,
            responseBody = response.body,
            titleFailList = incorrectData.title,
            subgroupFailList = incorrectData.subgroup,
            nonFieldFailList = incorrectData.nonFieldErrors
        )
    }

    private fun handleCreateFail(
        responseCode: Int,
        responseBody: String,
        titleFailList: List<String>,
        subgroupFailList: List<String>,
        nonFieldFailList: List<String>
    ): RequestFailure<List<SubscriptionFail>> {
        val failList = titleFailList.map {
            when (it) {
                "max_length" -> SubscriptionFail.TooLongTitle
                else -> DataFailure.UnknownResponse(responseCode, responseBody)
            }
        }.plus(
            subgroupFailList.map {
                when (it) {
                    "does_not_exist" -> SubscriptionFail.SubgroupDoesNotExist
                    else -> DataFailure.UnknownResponse(responseCode, responseBody)
                }
            }
        ).plus(
            nonFieldFailList.map {
                when (it) {
                    "unique" -> SubscriptionFail.SubscriptionAlreadyExists
                    else -> DataFailure.UnknownResponse(responseCode, responseBody)
                }
            }
        )

        return RequestFailure(
            domain = failList.filterIsInstance<SubscriptionFail>(),
            data = failList.filterIsInstance<DataFailure>()
        )
    }

    override suspend fun selectSubscriptionsForUser(): Either<DataFailure, List<SubscriptionNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<SubscriptionNetworkDto>>("$baseUrl/api/$apiVersion/subscriptions/")
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
        subscription: SubscriptionNetworkDto
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> =
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

    override suspend fun deleteSubscription(id: Int): Either<DataFailure, Unit> =
        with(networkSender) {
            handle {
                delete<Unit>("$baseUrl/api/$apiVersion/subscriptions/$id/")
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }
}