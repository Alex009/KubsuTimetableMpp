package com.kubsu.timetable.domain.interactor.subscription

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.NoActiveUserFailure
import com.kubsu.timetable.RequestFailure
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

    suspend fun create(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<NoActiveUserFailure>, Unit>

    suspend fun getById(id: Int): Either<RequestFailure<NoActiveUserFailure>, SubscriptionEntity>

    suspend fun getAll(): Either<RequestFailure<NoActiveUserFailure>, List<SubscriptionEntity>>

    suspend fun update(subscription: SubscriptionEntity): Either<NetworkFailure, Unit>

    suspend fun deleteById(id: Int): Either<NetworkFailure, Unit>
}