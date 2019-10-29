package com.kubsu.timetable.data.network.client.update

import com.egroden.teaco.Either
import com.egroden.teaco.mapLeft
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.diff.DiffResponse
import com.kubsu.timetable.data.network.dto.diff.SyncResponse
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import com.kubsu.timetable.extensions.addSessionKey
import com.kubsu.timetable.extensions.jsonContent
import com.kubsu.timetable.extensions.toJson
import io.ktor.client.request.post
import kotlinx.serialization.KSerializer
import kotlinx.serialization.list

class UpdateDataNetworkClientImpl(
    private val networkSender: NetworkSender
) : UpdateDataNetworkClient {
    override suspend fun diff(): Either<DataFailure, DiffResponse> =
        with(networkSender) {
            handle {
                post<DiffResponse>("$baseUrl/api/$apiVersion/university/diff/") {
                    addSessionKey(it)
                    body = jsonContent(
                        "timestamp" to (it?.timestamp?.value ?: 0).toJson()
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }

    override suspend fun sync(
        basename: String,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse> =
        with(networkSender) {
            handle {
                post<SyncResponse>("$baseUrl/api/$apiVersion/$basename/sync/") {
                    addSessionKey(it)
                    body = jsonContent(
                        "timestamp" to (it?.timestamp?.value ?: 0).toJson(),
                        "already_handled" to existsIds.toJson()
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
        basename: String,
        basenameSerializer: KSerializer<T>,
        updatedIds: List<Int>
    ): Either<DataFailure, List<T>> =
        with(networkSender) {
            handle {
                val response = post<String>("$baseUrl/api/$apiVersion/$basename/meta/") {
                    addSessionKey(it)
                    body = jsonContent(
                        "ids" to updatedIds.toJson()
                    )
                }
                networkSender.json.parse(basenameSerializer.list, response)
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated(it.body)
                else
                    toNetworkFail(it)
            }
        }
}