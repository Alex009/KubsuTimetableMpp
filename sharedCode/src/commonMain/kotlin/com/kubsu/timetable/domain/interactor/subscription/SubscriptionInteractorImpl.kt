package com.kubsu.timetable.domain.interactor.subscription

import com.kubsu.timetable.Either
import com.kubsu.timetable.def
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.auth.AuthGateway

class SubscriptionInteractorImpl(
    private val subscriptionGateway: SubscriptionGateway,
    private val authGateway: AuthGateway
) : SubscriptionInteractor {
    override suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyEntity>> = def {
        subscriptionGateway.selectFacultyList()
    }

    override suspend fun selectOccupationList(
        faculty: FacultyEntity
    ): Either<NetworkFailure, List<OccupationEntity>> = def {
        subscriptionGateway.selectOccupationList(faculty.id)
    }

    override suspend fun selectGroupList(
        occupation: OccupationEntity
    ): Either<NetworkFailure, List<GroupEntity>> = def {
        subscriptionGateway.selectGroupList(occupation.id)
    }

    override suspend fun selectSubgroupList(
        group: GroupEntity
    ): Either<NetworkFailure, List<SubgroupEntity>> = def {
        subscriptionGateway.selectSubgroupList(group.id)
    }

    override suspend fun createSubscription(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, Unit> = def {
        val user = authGateway.getUserOrNull()

        if (user != null)
            subscriptionGateway.createSubscription(user.id, subgroupId, subscriptionName, isMain)
        else
            Either.right(Unit) //TODO переделать как ошибку
    }

    override suspend fun getSubscriptionById(id: Int): Either<NetworkFailure, SubscriptionEntity> = def {
        subscriptionGateway.getSubscriptionById(id)
    }

    override suspend fun getAllSubscriptions(): Either<NetworkFailure, List<SubscriptionEntity>> = def {
        val user = authGateway.getUserOrNull()

        if (user != null)
            subscriptionGateway.getAllSubscriptions(user.id)
        else
            Either.right(emptyList()) //TODO переделать как ошибку
    }
}