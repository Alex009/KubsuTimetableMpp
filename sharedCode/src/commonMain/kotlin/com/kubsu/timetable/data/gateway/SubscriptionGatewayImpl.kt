package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.db.SubscriptionDb
import com.kubsu.timetable.data.db.SubscriptionQueries
import com.kubsu.timetable.data.mapper.timetable.data.SubscriptionMapper
import com.kubsu.timetable.data.mapper.timetable.select.FacultyMapper
import com.kubsu.timetable.data.mapper.timetable.select.GroupMapper
import com.kubsu.timetable.data.mapper.timetable.select.OccupationMapper
import com.kubsu.timetable.data.mapper.timetable.select.SubgroupMapper
import com.kubsu.timetable.data.network.NetworkClient
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionGateway

class SubscriptionGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val networkClient: NetworkClient
) : SubscriptionGateway {
    override suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyEntity>> =
        networkClient
            .selectFacultyList()
            .map { list -> list.map(FacultyMapper::toEntity) }

    override suspend fun selectOccupationList(facultyId: Int): Either<NetworkFailure, List<OccupationEntity>> =
        networkClient
            .selectOccupationList(facultyId)
            .map { list -> list.map { OccupationMapper.toEntity(it, facultyId) } }

    override suspend fun selectGroupList(occupationId: Int): Either<NetworkFailure, List<GroupEntity>> =
        networkClient
            .selectGroupList(occupationId)
            .map { list -> list.map { GroupMapper.toEntity(it, occupationId) } }

    override suspend fun selectSubgroupList(groupId: Int): Either<NetworkFailure, List<SubgroupEntity>> =
        networkClient
            .selectSubgroupList(groupId)
            .map { list -> list.map { SubgroupMapper.toEntity(it, groupId) } }

    override suspend fun createSubscription(
        userId: Int,
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, Unit> =
        networkClient
            .createSubscriptionAndReturnId(subgroupId, subscriptionName, isMain)
            .map { id ->
                subscriptionQueries.update(
                    SubscriptionDb.Impl(
                        id = id,
                        name = subscriptionName,
                        userId = userId,
                        subgroupId = subgroupId,
                        isMain = isMain
                    )
                )
            }


    override suspend fun getSubscriptionById(
        id: Int,
        userId: Int
    ): Either<NetworkFailure, SubscriptionEntity> {
        val subscription = subscriptionQueries.selectById(id).executeAsOneOrNull()

        return if (subscription != null)
            Either.right(SubscriptionMapper.toEntity(subscription))
        else
            networkClient
                .selectSubscriptionById(id)
                .map {
                    subscriptionQueries.update(SubscriptionMapper.toDbDto(it, userId))
                    SubscriptionMapper.toEntity(it, userId)
                }
    }

    override suspend fun getAllSubscriptions(userId: Int): Either<NetworkFailure, List<SubscriptionEntity>> {
        val subscriptionList = subscriptionQueries.selectByUserId(userId).executeAsList()

        return if (subscriptionList.isNotEmpty())
            Either.right(subscriptionList.map(SubscriptionMapper::toEntity))
        else
            networkClient
                .selectSubscriptionsForUser()
                .map { list ->
                    list.map {
                        subscriptionQueries.update(SubscriptionMapper.toDbDto(it, userId))
                        SubscriptionMapper.toEntity(it, userId)
                    }
                }
    }
}