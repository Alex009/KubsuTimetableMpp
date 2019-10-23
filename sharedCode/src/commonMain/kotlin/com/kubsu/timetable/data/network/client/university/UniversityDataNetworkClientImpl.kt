package com.kubsu.timetable.data.network.client.university

import com.egroden.teaco.Either
import com.egroden.teaco.bimap
import com.egroden.teaco.mapLeft
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.response.FantasticFour
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
                get<List<FacultyNetworkDto>>("$baseUrl/api/$apiVersion/faculties/")
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectOccupationList(facultyId: Int): Either<DataFailure, List<OccupationNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<OccupationNetworkDto>>(
                    "$baseUrl/api/$apiVersion/occupations/?faculty_id=$facultyId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectGroupList(occupationId: Int): Either<DataFailure, List<GroupNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<GroupNetworkDto>>(
                    "$baseUrl/api/$apiVersion/groups/?occupation_id=$occupationId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectSubgroupList(groupId: Int): Either<DataFailure, List<SubgroupNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<SubgroupNetworkDto>>(
                    "$baseUrl/api/$apiVersion/subgroups/?group_id=$groupId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectUniversityInfo(facultyId: Int): Either<DataFailure, UniversityInfoNetworkDto> =
        with(networkSender) {
            handle {
                get<FantasticFour>(
                    "$baseUrl/api/$apiVersion/faculties/$facultyId/info"
                )
            }
                .bimap(
                    leftOperation = ::toNetworkFail,
                    rightOperation = {
                        UniversityInfoNetworkDto(
                            id = it.id,
                            facultyId = it.objectId,
                            typeOfWeek = it
                                .data
                                .getValue("current_type_of_week")
                                .int
                        )
                    }
                )
        }
}