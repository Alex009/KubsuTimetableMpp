package com.kubsu.timetable.domain.interactor.subscription

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor

class SubscriptionInteractorImpl(
    private val subscriptionGateway: SubscriptionGateway,
    private val userInteractor: UserInteractor
) : SubscriptionInteractor {
    override suspend fun selectFacultyList(): Either<DataFailure, List<FacultyEntity>> = def {
        subscriptionGateway.selectFacultyList()
    }

    override suspend fun selectOccupationList(
        faculty: FacultyEntity
    ): Either<DataFailure, List<OccupationEntity>> = def {
        subscriptionGateway.selectOccupationList(faculty.id)
    }

    override suspend fun selectGroupList(
        occupation: OccupationEntity
    ): Either<DataFailure, List<GroupEntity>> = def {
        subscriptionGateway.selectGroupList(occupation.id)
    }

    override suspend fun selectSubgroupList(
        group: GroupEntity
    ): Either<DataFailure, List<SubgroupEntity>> = def {
        subscriptionGateway.selectSubgroupList(group.id)
    }

    override suspend fun create(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> = def {
        val user = userInteractor.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway.create(user.id, subgroupId, subscriptionName, isMain)
        else
            Either.left(RequestFailure(DataFailure.NotAuthenticated))
    }

    override suspend fun getById(id: Int): Either<DataFailure, SubscriptionEntity> = def {
        val user = userInteractor.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway.getById(id, user.id)
        else
            Either.left(DataFailure.NotAuthenticated)
    }

    override suspend fun getAll(): Either<DataFailure, List<SubscriptionEntity>> = def {
        val user = userInteractor.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway.getAll(user.id)
        else
            Either.left(DataFailure.NotAuthenticated)
    }

    override suspend fun update(
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> = def {
        subscriptionGateway.update(subscription)
    }

    override suspend fun deleteById(id: Int): Either<DataFailure, Unit> = def {
        subscriptionGateway.deleteById(id)
    }
}