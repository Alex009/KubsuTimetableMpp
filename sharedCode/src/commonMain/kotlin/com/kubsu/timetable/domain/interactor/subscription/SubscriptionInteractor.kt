package com.kubsu.timetable.domain.interactor.subscription

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import kotlinx.coroutines.flow.Flow

interface SubscriptionInteractor {
    suspend fun selectFacultyList(): Either<DataFailure, List<FacultyEntity>>

    suspend fun selectOccupationList(faculty: FacultyEntity): Either<DataFailure, List<OccupationEntity>>

    suspend fun selectGroupList(occupation: OccupationEntity): Either<DataFailure, List<GroupEntity>>

    suspend fun selectSubgroupList(group: GroupEntity): Either<DataFailure, List<SubgroupEntity>>

    suspend fun createSubscriptionTransaction(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionEntity>

    fun getAllSubscriptionsFlow(): Either<DataFailure, Flow<List<SubscriptionEntity>>>

    suspend fun update(
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit>

    suspend fun deleteById(id: Int): Either<DataFailure, Unit>
}