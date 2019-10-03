package com.kubsu.timetable.data.network.client.subscription.create

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.timetable.select.FacultyNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.GroupNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.OccupationNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.SubgroupNetworkDto

class CreateSubscriptionNetworkClientImpl : CreateSubscriptionNetworkClient {
    override suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectOccupationList(facultyId: Int): Either<NetworkFailure, List<OccupationNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectGroupList(occupationId: Int): Either<NetworkFailure, List<GroupNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectSubgroupList(groupId: Int): Either<NetworkFailure, List<SubgroupNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun createSubscriptionAndReturnId(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}