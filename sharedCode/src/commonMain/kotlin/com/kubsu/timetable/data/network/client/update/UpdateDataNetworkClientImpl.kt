package com.kubsu.timetable.data.network.client.update

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.response.DiffResponse
import com.kubsu.timetable.data.network.dto.response.SyncResponse
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.LecturerNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters

class UpdateDataNetworkClientImpl(
    private val networkSender: NetworkSender
) : UpdateDataNetworkClient {
    override suspend fun diff(timestamp: Long): Either<NetworkFailure, DiffResponse> =
        with(networkSender) {
            handle {
                post<DiffResponse>("$baseUrl/api/$apiVersion/university/diff/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("timestamp", timestamp.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun syncSubscription(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> =
        with(networkSender) {
            handle {
                post<SyncResponse>("$baseUrl/api/$apiVersion/subscriptions/sync/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("timestamp", timestamp.toString())
                            append("existing_ids", existsIds.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun syncTimetable(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> =
        with(networkSender) {
            handle {
                post<SyncResponse>("$baseUrl/api/$apiVersion/timetables/sync/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("timestamp", timestamp.toString())
                            append("existing_ids", existsIds.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun syncLecturer(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> =
        with(networkSender) {
            handle {
                post<SyncResponse>("$baseUrl/api/$apiVersion/lecturers/sync/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("timestamp", timestamp.toString())
                            append("existing_ids", existsIds.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun syncClass(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> =
        with(networkSender) {
            handle {
                post<SyncResponse>("$baseUrl/api/$apiVersion/classes/sync/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("timestamp", timestamp.toString())
                            append("existing_ids", existsIds.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun metaSubscription(
        updatedIds: List<Int>
    ): Either<NetworkFailure, List<SubscriptionNetworkDto>> =
        with(networkSender) {
            handle {
                post<List<SubscriptionNetworkDto>>("$baseUrl/api/$apiVersion/subscriptions/meta/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("ids", updatedIds.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun metaTimetable(
        updatedIds: List<Int>
    ): Either<NetworkFailure, List<TimetableNetworkDto>> =
        with(networkSender) {
            handle {
                post<List<TimetableNetworkDto>>("$baseUrl/api/$apiVersion/timetables/meta/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("ids", updatedIds.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun metaLecturer(
        updatedIds: List<Int>
    ): Either<NetworkFailure, List<LecturerNetworkDto>> =
        with(networkSender) {
            handle {
                post<List<LecturerNetworkDto>>("$baseUrl/api/$apiVersion/lecturers/meta/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("ids", updatedIds.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun metaClass(
        updatedIds: List<Int>
    ): Either<NetworkFailure, List<ClassNetworkDto>> =
        with(networkSender) {
            handle {
                post<List<ClassNetworkDto>>("$baseUrl/api/$apiVersion/classes/meta/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("ids", updatedIds.toString())
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }
}