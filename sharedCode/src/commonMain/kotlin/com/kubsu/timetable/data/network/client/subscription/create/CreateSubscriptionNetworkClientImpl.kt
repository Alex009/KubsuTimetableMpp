package com.kubsu.timetable.data.network.client.subscription.create

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.FacultyNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.GroupNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.OccupationNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.SubgroupNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.Parameters

class CreateSubscriptionNetworkClientImpl(
    private val networkSender: NetworkSender
) : CreateSubscriptionNetworkClient {
    override suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<FacultyNetworkDto>>("$baseUrl/api/$apiVersion/university/faculties/")
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectOccupationList(facultyId: Int): Either<NetworkFailure, List<OccupationNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<OccupationNetworkDto>>(
                    "$baseUrl/api/$apiVersion/university/occupations/?faculty_id=$facultyId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectGroupList(occupationId: Int): Either<NetworkFailure, List<GroupNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<GroupNetworkDto>>(
                    "$baseUrl/api/$apiVersion/university/groups/?occupation_id=$occupationId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun selectSubgroupList(groupId: Int): Either<NetworkFailure, List<SubgroupNetworkDto>> =
        with(networkSender) {
            handle {
                get<List<SubgroupNetworkDto>>(
                    "$baseUrl/api/$apiVersion/university/subgroups/?group_id=$groupId"
                )
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun createSubscription(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, SubscriptionNetworkDto> = with(networkSender) {
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
        }.mapLeft(::toNetworkFail)
    }
}