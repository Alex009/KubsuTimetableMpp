package com.kubsu.timetable.data.network.client.university

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.data.network.dto.timetable.data.UniversityInfoNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.FacultyNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.GroupNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.OccupationNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.SubgroupNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.get

class UniversityDataNetworkClientImpl(
    private val networkSender: NetworkSender
) : UniversityDataNetworkClient {
    override suspend fun selectFacultyList(): Either<DataFailure, List<FacultyNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<FacultyNetworkDto>>("$baseUrl/api/$apiVersion/university/faculties/")
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectOccupationList(facultyId: Int): Either<DataFailure, List<OccupationNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<OccupationNetworkDto>>(
                    "$baseUrl/api/$apiVersion/university/occupations/?faculty_id=$facultyId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectGroupList(occupationId: Int): Either<DataFailure, List<GroupNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<GroupNetworkDto>>(
                    "$baseUrl/api/$apiVersion/university/groups/?occupation_id=$occupationId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectSubgroupList(groupId: Int): Either<DataFailure, List<SubgroupNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<SubgroupNetworkDto>>(
                    "$baseUrl/api/$apiVersion/university/subgroups/?group_id=$groupId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectUniversityInfo(facultyId: Int): Either<DataFailure, UniversityInfoNetworkDto> =
        with(networkSender) {
            handle {
                get<UniversityInfoNetworkDto>(
                    "$baseUrl/api/$apiVersion/university-info/$facultyId"
                )
            }.mapLeft(::toNetworkFail)
        }
}