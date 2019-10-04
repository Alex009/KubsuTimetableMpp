package com.kubsu.timetable.domain.interactor.subscription

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.main.UserInteractor

class SubscriptionInteractorImpl(
    private val subscriptionGateway: SubscriptionGateway,
    private val userInteractor: UserInteractor
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

    override suspend fun create(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<NoActiveUserFailure>, Unit> = def {
        val user = userInteractor.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway
                .create(user.id, subgroupId, subscriptionName, isMain)
                .flatMapLeft { RequestFailure.eitherLeft(it) }
        else
            RequestFailure.eitherLeft(NoActiveUserFailure)
    }

    override suspend fun getById(id: Int): Either<RequestFailure<NoActiveUserFailure>, SubscriptionEntity> =
        def {
            val user = userInteractor.getCurrentUserOrNull()

            if (user != null)
                subscriptionGateway
                    .getById(id, user.id)
                    .flatMapLeft { RequestFailure.eitherLeft(it) }
            else
                RequestFailure.eitherLeft(NoActiveUserFailure)
    }

    override suspend fun getAll(): Either<RequestFailure<NoActiveUserFailure>, List<SubscriptionEntity>> =
        def {
            val user = userInteractor.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway
                .getAll(user.id)
                .flatMapLeft { RequestFailure.eitherLeft(it) }
        else
            RequestFailure.eitherLeft(NoActiveUserFailure)
        }

    override suspend fun update(
        subscription: SubscriptionEntity
    ): Either<NetworkFailure, Unit> = def {
        subscriptionGateway.update(subscription)
    }

    override suspend fun deleteById(id: Int): Either<NetworkFailure, Unit> = def {
        subscriptionGateway.deleteById(id)
    }
}