package com.kubsu.timetable.domain.interactor.subscription

import com.egroden.teaco.Either
import com.egroden.teaco.left
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.timetable.AppInfoGateway
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.def
import kotlinx.coroutines.flow.Flow

class SubscriptionInteractorImpl(
    private val subscriptionGateway: SubscriptionGateway,
    private val userInfoGateway: UserInfoGateway,
    private val appInfoGateway: AppInfoGateway
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

    override suspend fun createSubscriptionTransaction(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionEntity> = def {
        subscriptionGateway
            .createSubscriptionTransaction(
                subgroupId = subgroupId,
                subscriptionName = subscriptionName,
                isMain = isMain,
                withTransaction = { appInfoGateway.updateInfo(it.userId) }
            )
    }

    override fun getAllSubscriptionsFlow(): Either<DataFailure, Flow<List<SubscriptionEntity>>> =
        userInfoGateway
            .getCurrentUserOrNull()
            ?.let {
                Either.right(subscriptionGateway.getAllSubscriptionsFlow(it))
            }
            ?: Either.left(
                DataFailure.NotAuthenticated("SubscriptionInteractor#getAll")
            )

    override suspend fun update(
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> = def {
        subscriptionGateway.update(subscription)
    }

    override suspend fun deleteById(id: Int): Either<DataFailure, Unit> = def {
        subscriptionGateway.deleteById(id)
    }
}