package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.db.diff.DataDiffDb
import com.kubsu.timetable.data.db.diff.DataDiffQueries
import com.kubsu.timetable.data.db.diff.DeletedEntityQueries
import com.kubsu.timetable.data.db.diff.UpdatedEntityQueries
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.diff.BasenameDtoMapper
import com.kubsu.timetable.data.mapper.diff.DataDiffDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.data.network.client.update.UpdateDataNetworkClient
import com.kubsu.timetable.data.network.dto.timetable.data.*
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.session.getEitherFailure
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway
import com.kubsu.timetable.extensions.asFilteredFlowNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SyncMixinGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val lecturerQueries: LecturerQueries,
    private val classQueries: ClassQueries,
    private val dataDiffQueries: DataDiffQueries,
    private val universityInfoQueries: UniversityInfoQueries,
    private val updatedEntityQueries: UpdatedEntityQueries,
    private val deletedEntityQueries: DeletedEntityQueries,
    private val networkClient: UpdateDataNetworkClient,
    private val userStorage: UserStorage,
    private val sessionStorage: SessionStorage
) : SyncMixinGateway {
    override suspend fun registerDataDiff(entity: DataDiffEntity) {
        val id = dataDiffQueries.count().executeAsOne().toInt()

        dataDiffQueries.update(
            id = id,
            basename = BasenameDtoMapper.value(entity.basename),
            userId = entity.userId
        )

        for (updatedId in entity.updatedIds)
            updatedEntityQueries.update(id, updatedId)
        for (deletedId in entity.deletedIds)
            deletedEntityQueries.update(id, deletedId)
    }

    @UseExperimental(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun getAvailableDiffListFlowForCurrentUser(): Flow<List<DataDiffEntity>> =
        dataDiffQueries
            .selectAll()
            .asFilteredFlowNotNull { query ->
                val userId = userStorage.get()?.id ?: return@asFilteredFlowNotNull null
                val dataDiffList = query.executeAsList().filter { it.userId == userId }
                dataDiffList to userId
            }
            .map { (dataDiffList, userId) ->
                Basename
                    .list
                    .flatMap { basename ->
                        getIdsListFlow(userId, basename, dataDiffList)
                    }
            }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun getIdsListFlow(
        userId: Int,
        basename: Basename,
        dataDiffList: List<DataDiffDb>
    ): List<DataDiffEntity> {
        val basenameStr = BasenameDtoMapper.value(basename)
        val dataDiffIdList = dataDiffList
            .filter { it.basename == basenameStr }
            .map { it.id }

        return dataDiffIdList.map { id ->
            val updateIds = updatedEntityQueries
                .selectByDataDiffId(id)
                .executeAsList()
            val deletedIds = deletedEntityQueries
                .selectByDataDiffId(id)
                .executeAsList()
            DataDiffDtoMapper.toEntity(userId, basename, updateIds, deletedIds)
        }
    }
    override suspend fun delete(list: List<DataDiffEntity>) {
        for (diff in list) {
            deleteData(diff.basename, diff.deletedIds)

            val basenameStringify = BasenameDtoMapper.value(diff.basename)
            val diffId = dataDiffQueries
                .select(basenameStringify, diff.userId)
                .executeAsOne()
                .id
            deletedEntityQueries.deleteByDataDiffId(diffId)
            updatedEntityQueries.deleteByDataDiffId(diffId)
            dataDiffQueries.delete(basenameStringify, diff.userId)
        }
    }

    private fun deleteData(basename: Basename, deletedIds: List<Int>) =
        when (basename) {
            Basename.Subscription -> deletedIds.forEach(subscriptionQueries::deleteById)
            Basename.Timetable -> deletedIds.forEach(timetableQueries::deleteById)
            Basename.Lecturer -> deletedIds.forEach(lecturerQueries::deleteById)
            Basename.Class -> deletedIds.forEach(classQueries::deleteById)
            Basename.UniversityInfo -> deletedIds.forEach(universityInfoQueries::deleteById)
        }

    override suspend fun diff(): Either<DataFailure, Pair<Timestamp, List<Basename>>> =
        sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                networkClient
                    .diff(session)
                    .map {
                        val newTimestamp = Timestamp(it.timestamp)
                        val list = it.basenameList.map { basename ->
                            BasenameDtoMapper.toEntity(basename)
                        }
                        newTimestamp to list
                    }
            }

    override suspend fun updateData(
        basename: Basename,
        availableDiff: DataDiffEntity
    ): Either<DataFailure, Unit> {
        val existsIds = availableDiff.updatedIds + availableDiff.deletedIds
        return sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                networkClient
                    .sync(
                        session = session,
                        basename = BasenameDtoMapper.value(basename),
                        existsIds = existsIds
                    )
                    .flatMap { (updatedIds, deletedIds) ->
                        deleteData(basename, deletedIds)
                        meta(basename, updatedIds)
                    }
            }
    }

    override suspend fun meta(
        basename: Basename,
        updatedIds: List<Int>
    ): Either<DataFailure, Unit> =
        sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                val strBasename = BasenameDtoMapper.value(basename)
                when (basename) {
                    Basename.Subscription ->
                        networkClient
                            .meta(
                                session,
                                strBasename,
                                SubscriptionNetworkDto.serializer(),
                                updatedIds
                            )
                            .map { list ->
                                for (networkDto in list)
                                    subscriptionQueries.update(
                                        SubscriptionDtoMapper.toDbDto(networkDto)
                                    )
                            }

                    Basename.Timetable ->
                        networkClient
                            .meta(
                                session,
                                strBasename,
                                TimetableNetworkDto.serializer(),
                                updatedIds
                            )
                            .map { list ->
                                for (timetable in list)
                                    timetableQueries.update(TimetableDtoMapper.toDbDto(timetable))
                            }

                    Basename.Lecturer ->
                        networkClient
                            .meta(session, strBasename, LecturerNetworkDto.serializer(), updatedIds)
                            .map { list ->
                                for (lecturer in list)
                                    lecturerQueries.update(LecturerDtoMapper.toDbDto(lecturer))
                            }

                    Basename.Class ->
                        networkClient
                            .meta(session, strBasename, ClassNetworkDto.serializer(), updatedIds)
                            .map { list ->
                                for (`class` in list)
                                    classQueries.update(ClassDtoMapper.toDbDto(`class`))
                            }

                    Basename.UniversityInfo ->
                        networkClient
                            .meta(
                                session,
                                strBasename,
                                UniversityInfoNetworkDto.serializer(),
                                updatedIds
                            )
                            .map { list ->
                                for (info in list)
                                    universityInfoQueries.update(
                                        UniversityInfoDtoMapper.toDbDto(
                                            info
                                        )
                                    )
                            }
                }
            }
}