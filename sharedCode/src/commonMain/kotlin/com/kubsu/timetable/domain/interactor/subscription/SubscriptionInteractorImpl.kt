package com.kubsu.timetable.domain.interactor.subscription

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.main.MainInteractor

class SubscriptionInteractorImpl(
    private val subscriptionGateway: SubscriptionGateway,
    private val mainInteractor: MainInteractor
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
    ): Either<WrapperFailure<NoActiveUserFailure>, Unit> = def {
        val user = mainInteractor.getCurrentUser()

        if (user != null)
            subscriptionGateway
                .createSubscription(user.id, subgroupId, subscriptionName, isMain)
                .mapLeft { WrapperFailure(NoActiveUserFailure) }
        else
            Either.left(WrapperFailure(NoActiveUserFailure))
    }

    override suspend fun getSubscriptionById(id: Int): Either<WrapperFailure<NoActiveUserFailure>, SubscriptionEntity> =
        def {
            val user = mainInteractor.getCurrentUser()

            if (user != null)
                subscriptionGateway
                    .getSubscriptionById(id, user.id)
                    .mapLeft { WrapperFailure(NoActiveUserFailure) }
            else
                Either.left(WrapperFailure(NoActiveUserFailure))
    }

    override suspend fun getAllSubscriptions(): Either<WrapperFailure<NoActiveUserFailure>, List<SubscriptionEntity>> =
        def {
            val user = mainInteractor.getCurrentUser()

        if (user != null)
            subscriptionGateway
                .getAllSubscriptions(user.id)
                .mapLeft { WrapperFailure(NoActiveUserFailure) }
        else
            Either.left(WrapperFailure(NoActiveUserFailure))
    }
}