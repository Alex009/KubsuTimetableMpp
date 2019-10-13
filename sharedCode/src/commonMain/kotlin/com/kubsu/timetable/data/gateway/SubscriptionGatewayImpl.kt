package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.data.db.timetable.SubscriptionQueries
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.SubscriptionDtoMapper
import com.kubsu.timetable.data.mapper.timetable.select.FacultyDtoMapper
import com.kubsu.timetable.data.mapper.timetable.select.GroupDtoMapper
import com.kubsu.timetable.data.mapper.timetable.select.OccupationDtoMapper
import com.kubsu.timetable.data.mapper.timetable.select.SubgroupDtoMapper
import com.kubsu.timetable.data.network.client.subscription.SubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionGateway

class SubscriptionGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val universityDataNetworkClient: UniversityDataNetworkClient,
    private val subscriptionNetworkClient: SubscriptionNetworkClient
) : SubscriptionGateway {
    override suspend fun selectFacultyList(): Either<DataFailure, List<FacultyEntity>> =
        universityDataNetworkClient
            .selectFacultyList()
            .map { list -> list.map(FacultyDtoMapper::toEntity) }

    override suspend fun selectOccupationList(facultyId: Int): Either<DataFailure, List<OccupationEntity>> =
        universityDataNetworkClient
            .selectOccupationList(facultyId)
            .map { list -> list.map { OccupationDtoMapper.toEntity(it, facultyId) } }

    override suspend fun selectGroupList(occupationId: Int): Either<DataFailure, List<GroupEntity>> =
        universityDataNetworkClient
            .selectGroupList(occupationId)
            .map { list -> list.map { GroupDtoMapper.toEntity(it, occupationId) } }

    override suspend fun selectSubgroupList(groupId: Int): Either<DataFailure, List<SubgroupEntity>> =
        universityDataNetworkClient
            .selectSubgroupList(groupId)
            .map { list -> list.map { SubgroupDtoMapper.toEntity(it, groupId) } }

    override suspend fun create(
        user: UserEntity,
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> =
        subscriptionNetworkClient
            .createSubscription(
                user = UserDtoMapper.toNetworkDto(user),
                subgroupId = subgroupId,
                subscriptionName = subscriptionName,
                isMain = isMain
            )
            .map { subscription ->
                subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(subscription, user.id))
            }

    override suspend fun getById(id: Int, userId: Int): Either<DataFailure, SubscriptionEntity> {
        val subscription = subscriptionQueries.selectById(id).executeAsOneOrNull()

        return if (subscription != null)
            Either.right(SubscriptionDtoMapper.toEntity(subscription))
        else
            subscriptionNetworkClient
                .selectSubscriptionById(id)
                .map {
                    subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(it, userId))
                    SubscriptionDtoMapper.toEntity(it, userId)
                }
    }

    override suspend fun getAll(user: UserEntity): Either<DataFailure, List<SubscriptionEntity>> {
        val subscriptionList = subscriptionQueries.selectByUserId(user.id).executeAsList()

        return if (subscriptionList.isNotEmpty())
            Either.right(subscriptionList.map(SubscriptionDtoMapper::toEntity))
        else
            subscriptionNetworkClient
                .selectSubscriptionsForUser(UserDtoMapper.toNetworkDto(user))
                .map { list ->
                    list.map {
                        subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(it, user.id))
                        SubscriptionDtoMapper.toEntity(it, user.id)
                    }
                }
    }

    override suspend fun update(
        user: UserEntity,
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> =
        subscriptionNetworkClient
            .update(
                UserDtoMapper.toNetworkDto(user),
                SubscriptionDtoMapper.toNetworkDto(subscription)
            )
            .map {
                subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(subscription))
            }

    override suspend fun deleteById(
        user: UserEntity,
        id: Int
    ): Either<DataFailure, Unit> =
        subscriptionNetworkClient
            .deleteSubscription(UserDtoMapper.toNetworkDto(user), id)
            .map {
                subscriptionQueries.deleteById(id)
            }
}