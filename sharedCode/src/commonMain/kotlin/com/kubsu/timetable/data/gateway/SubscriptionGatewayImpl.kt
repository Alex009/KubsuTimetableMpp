package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.bimap
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.data.db.timetable.ClassQueries
import com.kubsu.timetable.data.db.timetable.SubscriptionDb
import com.kubsu.timetable.data.db.timetable.SubscriptionQueries
import com.kubsu.timetable.data.db.timetable.TimetableQueries
import com.kubsu.timetable.data.mapper.timetable.data.SubscriptionDtoMapper
import com.kubsu.timetable.data.mapper.timetable.select.FacultyDtoMapper
import com.kubsu.timetable.data.mapper.timetable.select.GroupDtoMapper
import com.kubsu.timetable.data.mapper.timetable.select.OccupationDtoMapper
import com.kubsu.timetable.data.mapper.timetable.select.SubgroupDtoMapper
import com.kubsu.timetable.data.network.client.subscription.SubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionGateway
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class SubscriptionGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val classQueries: ClassQueries,
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
        session: Session,
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean,
        withTransaction: suspend (SubscriptionEntity) -> Either<DataFailure, Unit>
    ): Either<RequestFailure<List<SubscriptionFail>>, SubscriptionEntity> =
        subscriptionNetworkClient
            .createSubscription(
                subgroupId = subgroupId,
                subscriptionName = subscriptionName,
                isMain = isMain,
                session = session
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

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun getAllSubscriptionsFlow(
        user: UserEntity
    ): Flow<List<SubscriptionEntity>> =
        subscriptionQueries
            .selectByUserId(user.id)
            .asFlow()
            .mapToList()
            .flatMapLatest { toSubscriptionEntityList(it) }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun toSubscriptionEntityList(
        subscriptionList: List<SubscriptionDb>
    ): Flow<List<SubscriptionEntity>> {
        val flows = subscriptionList.map { subscription ->
            numberOfUpdatedClassesFlow(subscription).map {
                SubscriptionDtoMapper.toEntity(subscription, it)
            }
        }
        return if (flows.isNotEmpty())
            combine(flows, transform = { it.toList() })
        else
            flowOf(emptyList())
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun numberOfUpdatedClassesFlow(subscription: SubscriptionDb): Flow<Long> {
        val flows = timetableQueries
            .selectBySubgroupId(subscription.subgroupId)
            .executeAsList()
            .map {
                classQueries
                    .countUpdatedForTimetable(it.id)
                    .asFlow()
                    .mapToOne()
            }
        return if (flows.isNotEmpty())
            combine(flows, transform = { it.sum() })
        else
            flowOf(0)
    }

    override suspend fun update(
        session: Session,
        subscription: SubscriptionEntity
    ): Either<RequestFailure<List<SubscriptionFail>>, Unit> =
        subscriptionNetworkClient
            .update(
                session = session,
                subscription = SubscriptionDtoMapper.toNetworkDto(subscription)
            )
            .map {
                subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(subscription))
            }

    override suspend fun deleteById(session: Session, id: Int): Either<DataFailure, Unit> =
        subscriptionNetworkClient
            .deleteSubscription(session = session, id = id)
            .map {
                subscriptionQueries.deleteById(id)
            }
}