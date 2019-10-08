package com.kubsu.timetable.data.network.client.update

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.data.network.dto.response.DiffResponse
import com.kubsu.timetable.data.network.dto.response.SyncResponse
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters

class UpdateDataNetworkClientImpl(
    private val networkSender: NetworkSender
) : UpdateDataNetworkClient {
    override suspend fun diff(timestamp: Long): Either<DataFailure, DiffResponse> =
        with(networkSender) {
            handle {
                post<DiffResponse>("$baseUrl/api/$apiVersion/university/diff/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("timestamp", timestamp.toString())
                        }
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated
                else
                    toNetworkFail(it)
            }
        }

    override suspend fun sync(
        basename: String,
        timestamp: Long,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse> =
        with(networkSender) {
            handle {
                post<SyncResponse>(
                    "$baseUrl/api/$apiVersion/$basename/sync/"
                ) {
                    body = FormDataContent(
                        Parameters.build {
                            append("timestamp", timestamp.toString())
                            append("existing_ids", existsIds.toString())
                        }
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated
                else
                    toNetworkFail(it)
            }
        }

    override suspend fun <T> meta(
        basename: String,
        updatedIds: List<Int>
    ): Either<DataFailure, List<T>> =
        with(networkSender) {
            handle {
                post<List<T>>("$baseUrl/api/$apiVersion/$basename/meta/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("ids", updatedIds.toString())
                        }
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 401)
                    DataFailure.NotAuthenticated
                else
                    toNetworkFail(it)
            }
        }
}