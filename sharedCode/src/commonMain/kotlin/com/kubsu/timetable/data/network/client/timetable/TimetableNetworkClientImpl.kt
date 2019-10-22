package com.kubsu.timetable.data.network.client.timetable

import com.egroden.teaco.Either
import com.egroden.teaco.mapLeft
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.ClassTimeNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.LecturerNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.get

class TimetableNetworkClientImpl(
    private val networkSender: NetworkSender
) : TimetableNetworkClient {
    override suspend fun selectTimetableList(
        subgroupId: Int
    ): Either<DataFailure, List<TimetableNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<TimetableNetworkDto>>("$baseUrl/api/$apiVersion/timetables/?subgroup_id=$subgroupId")
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectClassesByTimetableId(
        timetableId: Int
    ): Either<DataFailure, List<ClassNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<ClassNetworkDto>>("$baseUrl/api/$apiVersion/classes/?timetable_id=$timetableId")
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectLecturerById(id: Int): Either<DataFailure, LecturerNetworkDto> =
        with(networkSender) {
            handle {
                get<LecturerNetworkDto>("$baseUrl/api/$apiVersion/lecturers/$id/")
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectClassTimeById(id: Int): Either<DataFailure, ClassTimeNetworkDto> =
        with(networkSender) {
            handle {
                get<ClassTimeNetworkDto>("$baseUrl/api/$apiVersion/class-times/$id/")
            }.mapLeft(::toNetworkFail)
        }
}