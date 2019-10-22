package com.kubsu.timetable.data.network.client.update

import com.egroden.teaco.Either
import com.egroden.teaco.mapLeft
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.dto.response.DiffResponse
import com.kubsu.timetable.data.network.dto.response.SyncResponse
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import com.kubsu.timetable.extensions.addSessionKey
import com.kubsu.timetable.extensions.jsonContent
import com.kubsu.timetable.extensions.toJson
import io.ktor.client.request.post

class UpdateDataNetworkClientImpl(
    private val networkSender: NetworkSender
) : UpdateDataNetworkClient {
    override suspend fun diff(
        user: UserNetworkDto,
        timestamp: Long
    ): Either<DataFailure, DiffResponse> =
        with(networkSender) {
            handle {
                post<DiffResponse>("$baseUrl/api/$apiVersion/university/diff/") {
                    addSessionKey(user)
                    body = jsonContent("timestamp" to timestamp.toJson())
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }

    override suspend fun sync(
        user: UserNetworkDto,
        basename: String,
        timestamp: Long,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse> =
        with(networkSender) {
            handle {
                post<SyncResponse>("$baseUrl/api/$apiVersion/$basename/sync/") {
                    addSessionKey(user)
                    body = jsonContent(
                        "timestamp" to timestamp.toJson(),
                        "existing_ids" to existsIds.toJson()
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }

    override suspend fun <T> meta(
        user: UserNetworkDto,
        basename: String,
        updatedIds: List<Int>
    ): Either<DataFailure, List<T>> =
        with(networkSender) {
            handle {
                post<List<T>>("$baseUrl/api/$apiVersion/$basename/meta/") {
                    addSessionKey(user)
                    body = jsonContent("ids" to updatedIds.toJson())
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }
}