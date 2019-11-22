package com.kubsu.timetable.domain.interactor.subscription

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.egroden.teaco.mapLeft
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoGateway
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
        userInfoGateway
            .getCurrentSessionEitherFail()
            .mapLeft { RequestFailure<List<SubscriptionFail>>(it) }
            .flatMap { session ->
                subscriptionGateway
                    .createSubscriptionTransaction(
                        session = session,
                        subgroupId = subgroupId,
                        subscriptionName = subscriptionName,
                        isMain = isMain,
                        withTransaction = {
                            appInfoGateway.checkAvailabilityOfUserInfo(
                                session,
                                it.userId
                            )
                        }
                    )
            }
    }

    override fun getAllSubscriptionsFlow(): Either<DataFailure, Flow<List<SubscriptionEntity>>> =
        userInfoGateway
            .getCurrentUserEitherFailure()
            .map(subscriptionGateway::getAllSubscriptionsFlow)

    override suspend fun update(
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> = def {
        userInfoGateway
            .getCurrentSessionEitherFail()
            .mapLeft { RequestFailure<List<SubscriptionFail>>(it) }
            .flatMap { subscriptionGateway.update(it, subscription) }
    }

    override suspend fun deleteById(id: Int): Either<DataFailure, Unit> = def {
        userInfoGateway
            .getCurrentSessionEitherFail()
            .flatMap {
                subscriptionGateway.deleteById(it, id)
            }
    }
}