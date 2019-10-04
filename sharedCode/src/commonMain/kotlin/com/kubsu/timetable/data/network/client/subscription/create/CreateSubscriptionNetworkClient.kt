package com.kubsu.timetable.data.network.client.subscription.create

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.FacultyNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.GroupNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.OccupationNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.SubgroupNetworkDto

interface CreateSubscriptionNetworkClient {
    suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyNetworkDto>>
    suspend fun selectOccupationList(facultyId: Int): Either<NetworkFailure, List<OccupationNetworkDto>>
    suspend fun selectGroupList(occupationId: Int): Either<NetworkFailure, List<GroupNetworkDto>>
    suspend fun selectSubgroupList(groupId: Int): Either<NetworkFailure, List<SubgroupNetworkDto>>
    suspend fun createSubscription(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, SubscriptionNetworkDto>
}