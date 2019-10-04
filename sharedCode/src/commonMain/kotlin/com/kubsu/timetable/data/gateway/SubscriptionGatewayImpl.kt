package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.db.timetable.SubscriptionQueries
import com.kubsu.timetable.data.mapper.timetable.data.SubscriptionMapper
import com.kubsu.timetable.data.mapper.timetable.select.FacultyMapper
import com.kubsu.timetable.data.mapper.timetable.select.GroupMapper
import com.kubsu.timetable.data.mapper.timetable.select.OccupationMapper
import com.kubsu.timetable.data.mapper.timetable.select.SubgroupMapper
import com.kubsu.timetable.data.network.client.subscription.control.ControlSubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.subscription.create.CreateSubscriptionNetworkClient
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionGateway

class SubscriptionGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val createNetworkClient: CreateSubscriptionNetworkClient,
    private val controlNetworkClient: ControlSubscriptionNetworkClient
) : SubscriptionGateway {
    override suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyEntity>> =
        createNetworkClient
            .selectFacultyList()
            .map { list -> list.map(FacultyMapper::toEntity) }

    override suspend fun selectOccupationList(facultyId: Int): Either<NetworkFailure, List<OccupationEntity>> =
        createNetworkClient
            .selectOccupationList(facultyId)
            .map { list -> list.map { OccupationMapper.toEntity(it, facultyId) } }

    override suspend fun selectGroupList(occupationId: Int): Either<NetworkFailure, List<GroupEntity>> =
        createNetworkClient
            .selectGroupList(occupationId)
            .map { list -> list.map { GroupMapper.toEntity(it, occupationId) } }

    override suspend fun selectSubgroupList(groupId: Int): Either<NetworkFailure, List<SubgroupEntity>> =
        createNetworkClient
            .selectSubgroupList(groupId)
            .map { list -> list.map { SubgroupMapper.toEntity(it, groupId) } }

    override suspend fun create(
        userId: Int,
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, Unit> =
        createNetworkClient
            .createSubscription(subgroupId, subscriptionName, isMain)
            .map { subscription ->
                subscriptionQueries.update(SubscriptionMapper.toDbDto(subscription, userId))
            }

    override suspend fun getById(
        id: Int,
        userId: Int
    ): Either<NetworkFailure, SubscriptionEntity> {
        val subscription = subscriptionQueries.selectById(id).executeAsOneOrNull()

        return if (subscription != null)
            Either.right(SubscriptionMapper.toEntity(subscription))
        else
            controlNetworkClient
                .selectSubscriptionById(id)
                .map {
                    subscriptionQueries.update(SubscriptionMapper.toDbDto(it, userId))
                    SubscriptionMapper.toEntity(it, userId)
                }
    }

    override suspend fun getAll(userId: Int): Either<NetworkFailure, List<SubscriptionEntity>> {
        val subscriptionList = subscriptionQueries.selectByUserId(userId).executeAsList()

        return if (subscriptionList.isNotEmpty())
            Either.right(subscriptionList.map(SubscriptionMapper::toEntity))
        else
            controlNetworkClient
                .selectSubscriptionsForUser()
                .map { list ->
                    list.map {
                        subscriptionQueries.update(SubscriptionMapper.toDbDto(it, userId))
                        SubscriptionMapper.toEntity(it, userId)
                    }
                }
    }

    override suspend fun update(subscription: SubscriptionEntity): Either<NetworkFailure, Unit> =
        controlNetworkClient
            .update(SubscriptionMapper.toNetworkDto(subscription))
            .map {
                subscriptionQueries.update(SubscriptionMapper.toDbDto(subscription))
            }

    override suspend fun deleteById(id: Int): Either<NetworkFailure, Unit> =
        controlNetworkClient
            .deleteSubscription(id)
            .map {
                subscriptionQueries.deleteById(id)
            }
}