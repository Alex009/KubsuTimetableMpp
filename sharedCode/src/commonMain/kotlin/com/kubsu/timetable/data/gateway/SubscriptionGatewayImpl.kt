package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.egroden.teaco.mapLeft
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
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.session.getEitherFailure
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionGateway
import com.kubsu.timetable.extensions.getContentFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubscriptionGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val universityDataNetworkClient: UniversityDataNetworkClient,
    private val subscriptionNetworkClient: SubscriptionNetworkClient,
    private val sessionStorage: SessionStorage
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
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionEntity> =
        sessionStorage
            .getEitherFailure()
            .mapLeft { RequestFailure<List<SubscriptionFail>>(it) }
            .flatMap { session ->
                subscriptionNetworkClient
                    .createSubscription(
                        session = session,
                        subgroupId = subgroupId,
                        subscriptionName = subscriptionName,
                        isMain = isMain
                    )
                    .map { subscription ->
                        subscription.user
                        subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(subscription))
                        SubscriptionDtoMapper.toEntity(subscription)
                    }
            }

    override fun getAllSubscriptionsFlow(
        user: UserEntity
    ): Flow<List<SubscriptionEntity>> =
        subscriptionQueries
            .selectByUserId(user.id)
            .getContentFlow { it.executeAsList() }
            .map { it.map(SubscriptionDtoMapper::toEntity) }

    override suspend fun update(
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> =
        sessionStorage
            .getEitherFailure()
            .mapLeft { RequestFailure<List<SubscriptionFail>>(it) }
            .flatMap { session ->
                subscriptionNetworkClient
                    .update(session, SubscriptionDtoMapper.toNetworkDto(subscription))
                    .map {
                        subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(subscription))
                    }
            }

    override suspend fun deleteById(id: Int): Either<DataFailure, Unit> =
        sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                subscriptionNetworkClient
                    .deleteSubscription(session, id)
                    .map {
                        subscriptionQueries.deleteById(id)
                    }
            }
}