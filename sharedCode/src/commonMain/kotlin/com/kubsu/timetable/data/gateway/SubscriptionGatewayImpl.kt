package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.bimap
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.data.db.timetable.SubscriptionQueries
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
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    override suspend fun createSubscriptionTransaction(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean,
        withTransaction: suspend (SubscriptionEntity) -> Either<DataFailure, Unit>
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionEntity> =
        subscriptionNetworkClient
            .createSubscription(
                subgroupId = subgroupId,
                subscriptionName = subscriptionName,
                isMain = isMain
            )
            .flatMap { networkDto ->
                val subscription = SubscriptionDtoMapper.toEntity(networkDto)
                withTransaction(subscription).bimap(
                    leftOperation = {
                        RequestFailure<List<SubscriptionFail>>(it)
                    },
                    rightOperation = {
                        subscriptionQueries.update(
                            SubscriptionDtoMapper.toDbDto(
                                subscription
                            )
                        )
                        return@bimap subscription
                    }
                )
            }

    override fun getAllSubscriptionsFlow(
        user: UserEntity
    ): Flow<List<SubscriptionEntity>> =
        subscriptionQueries
            .selectByUserId(user.id)
            .asFlow()
            .mapToList()
            .map { it.map(SubscriptionDtoMapper::toEntity) }

    override suspend fun update(
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> =
        subscriptionNetworkClient
            .update(SubscriptionDtoMapper.toNetworkDto(subscription))
            .map {
                subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(subscription))
            }

    override suspend fun deleteById(id: Int): Either<DataFailure, Unit> =
        subscriptionNetworkClient
            .deleteSubscription(id)
            .map {
                subscriptionQueries.deleteById(id)
            }
}