package com.kubsu.timetable.domain.interactor.subscription

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity

interface SubscriptionInteractor {
    suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyEntity>>

    suspend fun selectOccupationList(faculty: FacultyEntity): Either<NetworkFailure, List<OccupationEntity>>

    suspend fun selectGroupList(occupation: OccupationEntity): Either<NetworkFailure, List<GroupEntity>>

    suspend fun selectSubgroupList(group: GroupEntity): Either<NetworkFailure, List<SubgroupEntity>>

    suspend fun createSubscription(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, Unit>

    suspend fun getSubscriptionById(id: Int): Either<NetworkFailure, SubscriptionEntity>

    suspend fun getAllSubscriptions(): Either<NetworkFailure, List<SubscriptionEntity>>
}