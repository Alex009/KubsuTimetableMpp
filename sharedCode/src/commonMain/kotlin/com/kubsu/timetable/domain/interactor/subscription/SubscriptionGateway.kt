package com.kubsu.timetable.domain.interactor.subscription

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity

interface SubscriptionGateway {
    suspend fun selectFacultyList(): Either<DataFailure, List<FacultyEntity>>
    suspend fun selectOccupationList(facultyId: Int): Either<DataFailure, List<OccupationEntity>>
    suspend fun selectGroupList(occupationId: Int): Either<DataFailure, List<GroupEntity>>
    suspend fun selectSubgroupList(groupId: Int): Either<DataFailure, List<SubgroupEntity>>

    suspend fun create(
        user: UserEntity,
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionEntity>

    suspend fun getById(id: Int, userId: Int): Either<DataFailure, SubscriptionEntity>

    suspend fun getAll(user: UserEntity): Either<DataFailure, List<SubscriptionEntity>>

    suspend fun update(
        user: UserEntity,
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit>

    suspend fun deleteById(user: UserEntity, id: Int): Either<DataFailure, Unit>
}