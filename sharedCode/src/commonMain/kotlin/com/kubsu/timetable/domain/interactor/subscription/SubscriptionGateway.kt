package com.kubsu.timetable.domain.interactor.subscription

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity

interface SubscriptionGateway {
    suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyEntity>>
    suspend fun selectOccupationList(facultyId: Int): Either<NetworkFailure, List<OccupationEntity>>
    suspend fun selectGroupList(occupationId: Int): Either<NetworkFailure, List<GroupEntity>>
    suspend fun selectSubgroupList(groupId: Int): Either<NetworkFailure, List<SubgroupEntity>>

    suspend fun createSubscription(
        userId: Int,
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, Unit>

    suspend fun getSubscriptionById(
        id: Int,
        userId: Int
    ): Either<NetworkFailure, SubscriptionEntity>

    suspend fun getAllSubscriptions(userId: Int): Either<NetworkFailure, List<SubscriptionEntity>>
}