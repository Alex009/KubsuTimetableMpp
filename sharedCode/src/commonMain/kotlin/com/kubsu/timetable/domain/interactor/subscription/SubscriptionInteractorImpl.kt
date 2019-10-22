package com.kubsu.timetable.domain.interactor.subscription

import com.egroden.teaco.Either
import com.egroden.teaco.left
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.def

class SubscriptionInteractorImpl(
    private val subscriptionGateway: SubscriptionGateway,
    private val userInfoGateway: UserInfoGateway
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
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionEntity> = def {
        val user = userInfoGateway.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway.create(user, subgroupId, subscriptionName, isMain)
        else
            Either.left(
                RequestFailure(
                    DataFailure.NotAuthenticated("SubscriptionInteractor#create")
                )
            )
    }

    override suspend fun getById(id: Int): Either<DataFailure, SubscriptionEntity> = def {
        val user = userInfoGateway.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway.getById(id, user.id)
        else
            Either.left(
                DataFailure.NotAuthenticated("SubscriptionInteractor#getById")
            )
    }

    override suspend fun getAll(): Either<DataFailure, List<SubscriptionEntity>> = def {
        val user = userInfoGateway.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway.getAll(user)
        else
            Either.left(
                DataFailure.NotAuthenticated("SubscriptionInteractor#getAll")
            )
    }

    override suspend fun update(
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> = def {
        val user = userInfoGateway.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway.update(user, subscription)
        else
            Either.left(
                RequestFailure(
                    DataFailure.NotAuthenticated("SubscriptionInteractor#update")
                )
            )
    }

    override suspend fun deleteById(id: Int): Either<DataFailure, Unit> = def {
        val user = userInfoGateway.getCurrentUserOrNull()

        if (user != null)
            subscriptionGateway.deleteById(user, id)
        else
            Either.left(
                DataFailure.NotAuthenticated("SubscriptionInteractor#deleteById")
            )
    }
}